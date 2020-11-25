package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.gyenes.paintball.app.repository.ReservationRepository

class ReservationViewModelProviderFactory(
    private val reservationRepository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReservationViewModel(reservationRepository) as T
    }
}