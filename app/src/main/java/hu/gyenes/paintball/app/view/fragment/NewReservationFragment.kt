package hu.gyenes.paintball.app.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.extensions.intersect
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import hu.gyenes.paintball.app.view.activiy.MainActivity
import hu.gyenes.paintball.app.view.viewmodel.GamePackageViewModel
import hu.gyenes.paintball.app.view.viewmodel.NoDateViewModel
import hu.gyenes.paintball.app.view.viewmodel.ReservationViewModel
import kotlinx.android.synthetic.main.fragment_new_reservation.*
import java.util.*

class NewReservationFragment : Fragment(R.layout.fragment_new_reservation) {

    private val args: NewReservationFragmentArgs by navArgs()
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var gamePackageViewModel: GamePackageViewModel
    private lateinit var noDateViewModel: NoDateViewModel
    private var updatedReservation: Reservation? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).syncAllRepositories()

        updatedReservation = args.reservation

        if (updatedReservation != null) {
            fillFormInputsFromReservation()
        }

        reservationViewModel = (activity as MainActivity).reservationViewModel
        gamePackageViewModel = (activity as MainActivity).gamePackageViewModel
        noDateViewModel = (activity as MainActivity).noDateViewModel

        btnSaveReservation.setOnClickListener {
            if (validateAll()) {
                saveReservation()
                //TODO navigateToReservationDetails
            }
        }
    }

    private fun saveReservation() {
        val date =
            Calendar.getInstance()
        date.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, timePicker.hour, 0)
        val reservation = Reservation(
            Date().time.toString(),
            etReservationName.text.toString(),
            etReservationEmail.text.toString(),
            etReservationPhoneNumber.text.toString(),
            etReservationPlayerNumber.text.toString().toInt(),
            date.time,
            gamePackageViewModel.getIdForPlayerNum(etReservationPlayerNumber.text.toString().toInt()),
            null,
            notes = etReservationNotes.text.toString(),
            syncState = ServerSyncState.NEW
        )
        if (updatedReservation != null) {
            reservation.id = updatedReservation!!.id
            reservation.timeStamp = updatedReservation!!.timeStamp
            if (updatedReservation?.syncState != ServerSyncState.NEW)
                reservation.syncState = ServerSyncState.UPDATED
            reservationViewModel.updateReservation(reservation)
            Log.e("reservation", "updated")
        } else {
            reservationViewModel.insertNewReservation(reservation)
            Log.e("reservation", "inserted")
        }
    }

    private fun fillFormInputsFromReservation() {
        etReservationName.setText(updatedReservation?.name)
        etReservationEmail.setText(updatedReservation?.email)
        etReservationPhoneNumber.setText(updatedReservation?.phoneNumber)
        etReservationPlayerNumber.setText(updatedReservation?.playerNumber.toString())
        etReservationNotes.setText(updatedReservation?.notes)
        if (updatedReservation?.date != null) {
            val cal = Calendar.getInstance()
            cal.time = updatedReservation?.date!!
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)) {_, _, _, _ ->}
            timePicker.hour = cal.get(Calendar.HOUR)
            timePicker.minute = cal.get(Calendar.MINUTE)
        }
    }

    private fun validateAll(): Boolean {
        return when {
            etReservationName.text.isEmpty() -> {
                etReservationName.requestFocus()
                etReservationName.error = "Name has to be given!"
                false
            }
            etReservationEmail.text.isEmpty() -> {
                etReservationEmail.requestFocus()
                etReservationEmail.error = "Email has to be given!"
                false
            }
            etReservationPhoneNumber.text.isEmpty() -> {
                etReservationPhoneNumber.requestFocus()
                etReservationPhoneNumber.error = "Phone number has to be given!"
                false
            }
            etReservationPlayerNumber.text.isEmpty() -> {
                etReservationPlayerNumber.requestFocus()
                etReservationPlayerNumber.error = "Player number has to be given!"
                false
            }
            gamePackageViewModel.getIdForPlayerNum(etReservationPlayerNumber.text.toString().toInt()) == "" -> {
                etReservationPlayerNumber.requestFocus()
                etReservationPlayerNumber.error = "No package for given player number!"
                false
            }
            !validateDatePicker(datePicker.year, datePicker.month, datePicker.dayOfMonth) -> {
                Toast.makeText(requireContext(), "Date is closed", Toast.LENGTH_LONG).show()
                false
            }
            !validateTimePicker(timePicker.hour) -> {
                Toast.makeText(requireContext(), "Time is already reserved", Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }

    private fun validateTimePicker(hourOfDay: Int): Boolean {
        val datePickerCalendarFrom = Calendar.getInstance()
        datePickerCalendarFrom.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, hourOfDay, 0)
        val datePickerCalendarTo = Calendar.getInstance()
        datePickerCalendarTo.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, hourOfDay + 2, 0)

        var playerCount = 0
        val overlapReservations = reservationViewModel.reservations.count { reservation ->
            var reserved = false
            if (reservation.date != null) {
                val fromDate = Calendar.getInstance()
                fromDate.time = reservation.date
                val toDate = Calendar.getInstance()
                toDate.time = reservation.date
                toDate.add(Calendar.HOUR, 2)
                if (!(updatedReservation != null && updatedReservation!!.id == reservation.id)) {
                    if ((datePickerCalendarFrom..datePickerCalendarTo).intersect(fromDate..toDate)) {
                        reserved = true
                        playerCount += reservation.playerNumber
                    }
                }
            }
            reserved
        }
        return overlapReservations <= 1 && playerCount <= 35
    }

    private fun validateDatePicker(year: Int, monthOfYear: Int, dayOfMonth: Int): Boolean {
        val datePickerCalendar = Calendar.getInstance()
        datePickerCalendar.set(year, monthOfYear, dayOfMonth, 12, 0)
        val fromDate = Calendar.getInstance()
        val toDate = Calendar.getInstance()
        val nodates = noDateViewModel.noDates
        val count = noDateViewModel.noDates.count { noDate ->
            var cover = false
            if (noDate.fromDate != null && noDate.toDate != null) {
                fromDate.time = noDate.fromDate
                fromDate.set(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH), fromDate.get(Calendar.DATE),
                3, 0)
                toDate.time = noDate.toDate
                toDate.set(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH), toDate.get(Calendar.DATE),
                    22, 0)
                cover = datePickerCalendar in fromDate..toDate
            }
            cover
        }
        return count == 0
    }
}