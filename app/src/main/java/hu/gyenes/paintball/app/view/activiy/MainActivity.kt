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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val syncRepositoryList: MutableList<Synchronizable> = mutableListOf()

        val reservationRepository = ReservationRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(reservationRepository)
        val gamePackageRepository = GamePackageRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(gamePackageRepository)
        val noDateRepository = NoDateRepository(GyenesPaintballDatabase(this))

        GlobalScope.launch {
            syncRepositoryList.forEach { it.sync() }
        }

//        syncRepositoryList.add(noDateRepository)

//        val reservations: MutableList<Reservation> = mutableListOf()
//        for (i in 0..10) {
//            val reservation = Reservation(i.toString(), "Morvai Ákos", "morvaiakos@asdlf.com", "021234123", 16, Date(), 10.toString(), Date())
//            reservations.add(reservation)
//        }
//        val packages: MutableList<GamePackage> = mutableListOf()
//        packages.add(GamePackage(40.toString(), 10, 20, 15, 3000, 2, false, 0 ))
//        GlobalScope.launch {
//            reservationRepository.insertAll(reservations)
//            gamePackageRepository.insertAll(packages)
//        }
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
}