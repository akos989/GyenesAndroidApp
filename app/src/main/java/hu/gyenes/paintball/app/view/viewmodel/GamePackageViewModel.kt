package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.*
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.repository.GamePackageRepository

class GamePackageViewModel(val gamePackageRepository: GamePackageRepository) : ViewModel() {

    private var gamePackages: List<GamePackage> = listOf()
    private val observer = Observer<List<GamePackage>> { packages ->
        gamePackages = packages
    }

    init {
        getPackagesFromDb().observeForever(observer)
    }

    fun findById(id: String) = gamePackages.find { p -> p.id == id }

    fun getPackagesFromDb() = gamePackageRepository.localFindAll()

    override fun onCleared() {
        super.onCleared()
        getPackagesFromDb().removeObserver(observer)
    }

    fun getIdForPlayerNum(playerNumber: Int): String {
        return gamePackages
            .find { p -> !p.disabled && playerNumber in p.fromNumberLimit..p.toNumberLimit }?.id ?: ""
    }
}

class GamePackageViewModelProviderFactory(
    private val gamePackageRepository: GamePackageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GamePackageViewModel(gamePackageRepository) as T
    }
}