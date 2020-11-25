package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.repository.GamePackageRepository
import kotlinx.coroutines.launch

class GamePackageViewModel(val gamePackageRepository: GamePackageRepository) : ViewModel() {

    var gamePackages: MutableList<GamePackage> = mutableListOf()

    init {
        getPackagesFromDb()
    }

    fun findById(id: String): GamePackage? {
        return gamePackages.find { p -> p.gamePackageId == id }
    }

    private fun getPackagesFromDb() = viewModelScope.launch {
        val packages = gamePackageRepository.findAll()
        gamePackages.addAll(packages)
    }
}