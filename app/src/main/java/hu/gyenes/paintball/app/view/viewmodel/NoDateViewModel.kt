package hu.gyenes.paintball.app.view.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hu.gyenes.paintball.app.model.NoDate
import hu.gyenes.paintball.app.repository.NoDateRepository

class NoDateViewModel(val noDateRepository: NoDateRepository) : ViewModel() {

    var noDates: List<NoDate> = listOf()
    private val observer = Observer<List<NoDate>> { noDates ->
        this.noDates = noDates
    }

    init {
        getNoDatesFromDb().observeForever(observer)
    }

    private fun getNoDatesFromDb() = noDateRepository.findAll()

    override fun onCleared() {
        super.onCleared()
        getNoDatesFromDb().removeObserver(observer)
    }
}

class NoDateViewModelProviderFactory(private val noDateRepository: NoDateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoDateViewModel(noDateRepository) as T
    }
}