package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.repository.ReservationRepository
import kotlinx.coroutines.launch

class ReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {

    var reservations: List<Reservation> = listOf()
    private val observer = Observer<List<Reservation>> { reservations ->
        this.reservations = reservations
    }

    init {
        getReservationsFromDb().observeForever(observer)
    }

    fun getReservationsFromDb() = reservationRepository.findAll()

    fun insertNewReservation(reservation: Reservation) = viewModelScope.launch {
        reservationRepository.insert(reservation)
    }
    fun updateReservation(reservation: Reservation) = viewModelScope.launch {
        reservationRepository.update(reservation)
    }
}

class ReservationViewModelProviderFactory(
    private val reservationRepository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationViewModel(reservationRepository) as T
    }
}