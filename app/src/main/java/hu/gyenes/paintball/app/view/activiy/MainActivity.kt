package hu.gyenes.paintball.app.view.activiy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.CurrentUser
import hu.gyenes.paintball.app.model.DTO.UserLoginRequest
import hu.gyenes.paintball.app.network.RetrofitInstance
import hu.gyenes.paintball.app.repository.*
import hu.gyenes.paintball.app.view.viewmodel.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var reservationViewModel: ReservationViewModel
    lateinit var gamePackageViewModel: GamePackageViewModel
    lateinit var noDateViewModel: NoDateViewModel
    lateinit var userViewModel: UserViewModel
    private val syncRepositoryList: MutableList<Synchronizable> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CurrentUser.initialize()

        val reservationRepository = ReservationRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(reservationRepository)
        val gamePackageRepository = GamePackageRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(gamePackageRepository)
        val noDateRepository = NoDateRepository(GyenesPaintballDatabase(this))
        syncRepositoryList.add(noDateRepository)
        val userRepository = UserRepository(GyenesPaintballDatabase(this))

//        if (!CurrentUser.isExpired())
//            syncAllRepositories()

        val reservationViewModelProviderFactory = ReservationViewModelProviderFactory(reservationRepository)
        reservationViewModel = ViewModelProvider(this, reservationViewModelProviderFactory)
            .get(ReservationViewModel::class.java)

        val gamePackageViewModelProviderFactory = GamePackageViewModelProviderFactory(gamePackageRepository)
        gamePackageViewModel = ViewModelProvider(this, gamePackageViewModelProviderFactory)
            .get(GamePackageViewModel::class.java)

        val noDateViewModelProviderFactory = NoDateViewModelProviderFactory(noDateRepository)
        noDateViewModel = ViewModelProvider(this, noDateViewModelProviderFactory)
            .get(NoDateViewModel::class.java)

        val userViewModelProviderFactory = UserViewModelProviderFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelProviderFactory)
            .get(UserViewModel::class.java)

        bottomNavigationView.setupWithNavController(paintballNavHostFragment.findNavController())
    }

    fun syncAllRepositories() {
        GlobalScope.launch {
            syncRepositoryList.forEach { it.sync() }
        }
    }
}