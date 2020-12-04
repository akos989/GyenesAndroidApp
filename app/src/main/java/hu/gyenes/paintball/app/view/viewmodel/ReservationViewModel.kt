package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.*
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.model.DTO.ReservationDeleteResponse
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import hu.gyenes.paintball.app.repository.ReservationRepository
import hu.gyenes.paintball.app.utils.PaintballApplication.Companion.context
import hu.gyenes.paintball.app.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {

    var reservations: List<Reservation> = listOf()
    private val observer = Observer<List<Reservation>> { reservations ->
        this.reservations = reservations
    }
    var reservationChange: MutableLiveData<Resource<Reservation>> = MutableLiveData()
    var reservationDelete: MutableLiveData<Resource<String>> = MutableLiveData()

    init {
        getReservationsFromDb().observeForever(observer)
    }

    fun getReservationsFromDb() = reservationRepository.findAll()

    fun insertNewReservation(reservation: Reservation) = viewModelScope.launch {
        reservationChange.postValue(Resource.Loading())
        val response = reservationRepository.insertRemote(reservation)
        val resource = handleReservationUpsertResponse(response, reservation)
        if (resource.data != null)
            reservationRepository.insert(resource.data)
        reservationChange.postValue(resource)
    }

    fun updateReservation(reservation: Reservation) = viewModelScope.launch {
        reservationChange.postValue(Resource.Loading())
        val response = reservationRepository.updateRemote(reservation.id, reservation)
        val resource = handleReservationUpsertResponse(response, reservation)
        if (resource.data != null)
            reservationRepository.update(resource.data)
        reservationChange.postValue(resource)
    }

    fun deleteReservation(reservation: Reservation) = viewModelScope.launch {
        if (reservation.syncState == ServerSyncState.NEW) {
            reservationRepository.delete(reservation)
        } else {
            val ids: MutableList<String> = mutableListOf()
            ids.add(reservation.id)
            val response = reservationRepository.deleteRemote(ids)
            val resource = handleReservationDeleteResponse(response, reservation)
            reservationDelete.postValue(resource)
        }
    }

    private fun deleteFromDb(reservation: Reservation) = viewModelScope.launch {
        reservationRepository.delete(reservation)
    }

    private fun updateInDb(reservation: Reservation) = viewModelScope.launch {
        reservationRepository.update(reservation)
    }

    private fun handleReservationDeleteResponse(response: Response<ReservationDeleteResponse>,
                                                originalReservation: Reservation): Resource<String> {
        if (response.isSuccessful) {
            response.body()?.let { _ ->
                deleteFromDb(originalReservation)
                return Resource.Success(context.getString(R.string.delete_from_server_success))
            }
        }
        if (response.code() == 400) {
            originalReservation.syncState = ServerSyncState.DELETED
            updateInDb(originalReservation)
            return Resource.Error("${response.message()} but marked as deleted locally!")
        }
        return Resource.Error(response.message())
    }

    private fun handleReservationUpsertResponse(response: Response<Reservation>, originalReservation: Reservation)
            : Resource<Reservation> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                resultResponse.syncState = ServerSyncState.REGISTERED
                return Resource.Success(resultResponse)
            }
        }
        if (response.code() == 400 ) {
            return Resource.Error(response.message(), originalReservation)
        }
        return Resource.Error(response.message())
    }

}

class ReservationViewModelProviderFactory(
    private val reservationRepository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationViewModel(reservationRepository) as T
    }
}