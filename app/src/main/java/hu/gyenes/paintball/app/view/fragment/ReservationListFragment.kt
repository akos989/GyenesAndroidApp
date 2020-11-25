package hu.gyenes.paintball.app.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.adapter.ReservationAdapter
import hu.gyenes.paintball.app.view.activiy.MainActivity
import hu.gyenes.paintball.app.view.viewmodel.ReservationViewModel
import kotlinx.android.synthetic.main.fragment_reservation_list.*

class ReservationListFragment : Fragment(R.layout.fragment_reservation_list) {
    lateinit var reservationViewModel: ReservationViewModel
    lateinit var reservationAdapter: ReservationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationViewModel = (activity as MainActivity).reservationViewModel
        setUpRecyclerView()
        reservationAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("reservation", it)
            }
            findNavController().navigate(
                R.id.action_reservationListFragment_to_reservationDetailFragment,
                bundle
            )
        }

        reservationViewModel.getAllReservations().observe(viewLifecycleOwner, { reservations ->
            reservationAdapter.differ.submitList(reservations)
        })
    }

    private fun setUpRecyclerView() {
        reservationAdapter = ReservationAdapter()
        rvAllReservationList.apply {
            adapter = reservationAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}