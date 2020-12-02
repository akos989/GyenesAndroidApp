package hu.gyenes.paintball.app.view.activiy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.repository.GamePackageRepository
import hu.gyenes.paintball.app.repository.NoDateRepository
import hu.gyenes.paintball.app.repository.ReservationRepository
import hu.gyenes.paintball.app.repository.Synchronizable
import hu.gyenes.paintball.app.view.viewmodel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var reservationViewModel: ReservationViewModel
    lateinit var gamePackageViewModel: GamePackageViewModel
    lateinit var noDateViewModel: NoDateViewModel
    private val syncRepositoryList: MutableList<Synchronizable> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reservationRepository = ReservationRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(reservationRepository)
        val gamePackageRepository = GamePackageRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(gamePackageRepository)
        val noDateRepository = NoDateRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(noDateRepository)

        syncAllRepositories()

        val reservationViewModelProviderFactory = ReservationViewModelProviderFactory(reservationRepository)
        reservationViewModel = ViewModelProvider(this, reservationViewModelProviderFactory)
            .get(ReservationViewModel::class.java)

        val gamePackageViewModelProviderFactory = GamePackageViewModelProviderFactory(gamePackageRepository)
        gamePackageViewModel = ViewModelProvider(this, gamePackageViewModelProviderFactory)
            .get(GamePackageViewModel::class.java)

        val noDateViewModelProviderFactory = NoDateViewModelProviderFactory(noDateRepository)
        noDateViewModel = ViewModelProvider(this, noDateViewModelProviderFactory)
            .get(NoDateViewModel::class.java)

        bottomNavigationView.setupWithNavController(paintballNavHostFragment.findNavController())
    }

    fun syncAllRepositories() {
        GlobalScope.launch {
            syncRepositoryList.forEach { it.sync() }
        }
    }
}