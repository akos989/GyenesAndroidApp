package hu.gyenes.paintball.app.view.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.adapter.ReservationAdapter
import hu.gyenes.paintball.app.model.CurrentUser
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.view.activiy.MainActivity
import hu.gyenes.paintball.app.view.viewmodel.ReservationViewModel
import kotlinx.android.synthetic.main.fragment_reservation_list.*


class ReservationListFragment : Fragment(R.layout.fragment_reservation_list) {
    lateinit var reservationViewModel: ReservationViewModel
    lateinit var reservationAdapter: ReservationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (CurrentUser.isExpired()) {
            findNavController().navigate(
                ReservationListFragmentDirections.actionReservationListFragmentToLoginFragment()
            )
        } else {
            (activity as MainActivity).syncAllRepositories()
        }

        reservationViewModel = (activity as MainActivity).reservationViewModel
        setUpRecyclerView()
        reservationAdapter.setOnClickListener { reservation ->
            val action = ReservationListFragmentDirections.actionReservationListFragmentToReservationDetailFragment(
                reservation = reservation
            )
            findNavController().navigate(action)
        }
        fabNewReservation.setOnClickListener {
            val action = ReservationListFragmentDirections.actionReservationListFragmentToNewReservationFragment(
                null
            )
            findNavController().navigate(action)
        }
        reservationViewModel.getReservationsFromDb().observe(viewLifecycleOwner, { reservations ->
            reservationAdapter.differ.submitList(reservations)
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val reservation = reservationAdapter.differ.currentList[position]
                buildAlertMessageDeleteReservation(reservation, position)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvAllReservationList)
        }
    }

    private fun setUpRecyclerView() {
        reservationAdapter = ReservationAdapter()
        rvAllReservationList.apply {
            adapter = reservationAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun buildAlertMessageDeleteReservation(reservation: Reservation, position: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.confirm_reservation_delete, reservation.name))
            .setOnCancelListener {
                reservationAdapter.notifyItemChanged(position)
            }
            .setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                reservationViewModel.deleteReservation(reservation)
            }
            .create()
            .show()
    }
}