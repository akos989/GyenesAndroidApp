package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.gyenes.paintball.app.repository.ReservationRepository

class ReservationViewModel(val reservationRepository: ReservationRepository) : ViewModel() {

//    val reservations: MutableLiveData<List<Reservation>> = MutableLiveData()
//
//    init {
//        getAllReservations()
//    }

    fun getAllReservations() = reservationRepository.findAll()
}

class ReservationViewModelProviderFactory(
    private val reservationRepository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationViewModel(reservationRepository) as T
    }
}