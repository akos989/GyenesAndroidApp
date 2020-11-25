package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.gyenes.paintball.app.repository.GamePackageRepository

class GamePackageViewModelProviderFactory(
    private val gamePackageRepository: GamePackageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GamePackageViewModel(gamePackageRepository) as T
    }
}