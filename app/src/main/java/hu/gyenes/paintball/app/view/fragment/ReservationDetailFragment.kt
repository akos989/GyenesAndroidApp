package hu.gyenes.paintball.app.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.view.activiy.MainActivity
import hu.gyenes.paintball.app.view.viewmodel.GamePackageViewModel
import kotlinx.android.synthetic.main.fragment_reservation_detail.*

class ReservationDetailFragment : Fragment(R.layout.fragment_reservation_detail) {

    lateinit var gamePackageViewModel: GamePackageViewModel
    private val args: ReservationDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamePackageViewModel = (activity as MainActivity).gamePackageViewModel

        val reservation = args.reservation
        val gamePackage: GamePackage? = gamePackageViewModel.findById(reservation.packageId)

        if (gamePackage != null) {
            reservationName.text = reservation.name
            reservationEmail.text = reservation.email
            reservationPlayerNumber.text = reservation.playerNumber.toString()
            reservationPhoneNumber.text = reservation.phoneNumber
            reservationDate.text = reservation.date?.toString()
            reservationPackagePrice.text = context?.getString(R.string.package_base_price_value, gamePackage.basePrice)
            reservationPackageBulletPrice.text = context?.getString(R.string.package_base_price_value, gamePackage.bulletPrice)
            reservationPackageBulletContained.text = context?.getString(R.string.package_bullet_contains_value, gamePackage.includedBullets)
            reservationPackageLength.text = context?.getString(R.string.package_game_length_value, gamePackage.duration)
            reservationCreatedAt.text = reservation.createdAt.toString()
        }
        //        else no package error
    }
}