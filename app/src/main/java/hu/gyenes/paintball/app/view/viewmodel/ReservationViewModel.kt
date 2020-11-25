package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.ViewModel
import hu.gyenes.paintball.app.repository.ReservationRepository

class ReservationViewModel(val reservationRepository: ReservationRepository) : ViewModel() {

//    val reservations: MutableLiveData<List<Reservation>> = MutableLiveData()
//
//    init {
//        getAllReservations()
//    }

    fun getAllReservations() = reservationRepository.findAll()
}