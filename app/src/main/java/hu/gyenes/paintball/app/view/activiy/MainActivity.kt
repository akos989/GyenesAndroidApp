package hu.gyenes.paintball.app.view.activiy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.repository.GamePackageRepository
import hu.gyenes.paintball.app.repository.ReservationRepository
import hu.gyenes.paintball.app.view.viewmodel.GamePackageViewModel
import hu.gyenes.paintball.app.view.viewmodel.GamePackageViewModelProviderFactory
import hu.gyenes.paintball.app.view.viewmodel.ReservationViewModel
import hu.gyenes.paintball.app.view.viewmodel.ReservationViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var reservationViewModel: ReservationViewModel
    lateinit var gamePackageViewModel: GamePackageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reservationRepository = ReservationRepository(GyenesPaintballDatabase(this))
        val gamePackageRepository = GamePackageRepository(GyenesPaintballDatabase(this))

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

        bottomNavigationView.setupWithNavController(paintballNavHostFragment.findNavController())
    }
}