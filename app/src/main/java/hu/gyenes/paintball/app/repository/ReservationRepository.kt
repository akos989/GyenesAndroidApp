package hu.gyenes.paintball.app.repository

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.provider.CalendarContract
import hu.gyenes.paintball.app.R
import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.DTO.ReservationDeleteRequest
import hu.gyenes.paintball.app.model.DTO.ReservationResponse
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import hu.gyenes.paintball.app.network.RetrofitInstance
import hu.gyenes.paintball.app.utils.PaintballApplication
import retrofit2.Response
import java.util.*

class ReservationRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insert(reservation: Reservation) {
        db.getReservationDao().insert(reservation)
    }

    suspend fun insertRemote(reservation: Reservation) =
        RetrofitInstance.reservationApi.insert(reservation)

    suspend fun update(reservation: Reservation) {
        db.getReservationDao().update(reservation)
        updateEvent(reservation)
    }

    suspend fun updateRemote(id: String, reservation: Reservation) =
        RetrofitInstance.reservationApi.update(id, reservation)

    suspend fun delete(reservation: Reservation) {
        deleteEvent(reservation)
        db.getReservationDao().delete(reservation)
    }

    suspend fun deleteRemote(ids: List<String>) = RetrofitInstance.reservationApi.delete(
        ReservationDeleteRequest(ids)
    )

    fun findAll() = db.getReservationDao().findAll()

    private suspend fun remoteFindAll() = RetrofitInstance.reservationApi.findReservations()

    private suspend fun insertAll(reservations: List<Reservation>) =
        db.getReservationDao().insertAll(
            reservations
        )

    override suspend fun sync() {
        syncPendingReservations()
        val reservations = handleFindAllResponse(remoteFindAll())
        if (reservations != null) {
            insertAll(reservations)
            addEvent(reservations.filter { r -> !r.archived })
        }
    }

    private suspend fun syncPendingReservations() {
        val toDeleteReservation: MutableList<Reservation> = mutableListOf()
        db.getReservationDao().findAllNotRegistered().forEach { reservation ->
            when (reservation.syncState) {
                ServerSyncState.NEW -> {
                    val response = insertRemote(reservation)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            db.getReservationDao().delete(reservation)
                            it.syncState = ServerSyncState.REGISTERED
                            db.getReservationDao().insert(it)
                        }
                    }
                }
                ServerSyncState.UPDATED -> {
                    val response = updateRemote(reservation.id, reservation)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.syncState = ServerSyncState.REGISTERED
                            db.getReservationDao().update(it)
                        }
                    }
                }
                ServerSyncState.DELETED -> {
                    toDeleteReservation.add(reservation)
                }
                else -> {
                }
            }
        }
        if (toDeleteReservation.size > 0) {
            val response = deleteRemote(toDeleteReservation.map { r -> r.id })
            if (response.isSuccessful) {
                response.body()?.let {
                    db.getReservationDao().deleteAll(toDeleteReservation)
                    toDeleteReservation.forEach { r -> deleteEvent(r) }
                }
            }
        }
    }

    private fun updateEvent(reservation: Reservation) {
        deleteEvent(reservation)
        val reservationList: MutableList<Reservation> = mutableListOf()
        reservationList.add(reservation)
        addEvent(reservationList)
    }

    private fun deleteEvent(reservation: Reservation) {
        val contentResolver = PaintballApplication.context.contentResolver
        val cur = isEventAlreadyExist(reservation)
        if (cur != null) {
            while (cur.moveToNext()) {
                // Get the field values
                val eventID = cur.getLong(0)
                var deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
                contentResolver.delete(deleteUri, null, null)
            }
        }
    }

    private fun handleFindAllResponse(response: Response<ReservationResponse>): List<Reservation>? {
        if (response.isSuccessful) {
            response.body()?.let { reservationResponse ->
                reservationResponse.reservations.forEach { r ->
                    if (r.notes == null)
                        r.notes = ""
                    r.syncState = ServerSyncState.REGISTERED
                }
                return reservationResponse.reservations
            }
        }
        return null
    }

    private fun addEvent(reservations: List<Reservation>) {
        val context = PaintballApplication.context
        reservations.forEach { reservation ->
            val cur = isEventAlreadyExist(reservation)
            var num = 0
            if (cur != null) {
                num = cur.count
                cur.close()
            }
            if (num <= 0) {
                val values = ContentValues()
                val cal = Calendar.getInstance()
                cal.time = reservation.date!!
                values.put(CalendarContract.Events.DTSTART, cal.timeInMillis)
                cal.add(Calendar.HOUR, 2)
                values.put(CalendarContract.Events.DTEND, cal.timeInMillis)
                values.put(CalendarContract.Events.TITLE, "${reservation.name} foglalás")
                values.put(
                    CalendarContract.Events.DESCRIPTION, """
                    ${context.getString(R.string.phoneNumber)}: ${reservation.phoneNumber}
                    ${context.getString(R.string.player_number)}: ${
                        context.getString(
                            R.string.player_number_value,
                            reservation.playerNumber
                        )
                    }
                    ${context.getString(R.string.email)}: ${reservation.email}
                """.trimIndent()
                )
                values.put(CalendarContract.Events.CALENDAR_ID, 1)
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                val contentResolver = PaintballApplication.context.contentResolver
                val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
            }
        }
    }

    private fun isEventAlreadyExist(reservation: Reservation): Cursor? {
        val INSTANCE_PROJECTION: MutableList<String> = mutableListOf()
        INSTANCE_PROJECTION.add(CalendarContract.Instances.EVENT_ID)
        INSTANCE_PROJECTION.add(CalendarContract.Instances.BEGIN)
        INSTANCE_PROJECTION.add(CalendarContract.Instances.TITLE)

        val startMillis: Long
        val endMillis: Long
        val beginTime = Calendar.getInstance()
        startMillis = beginTime.timeInMillis
        val endTime = Calendar.getInstance()
        endTime.add(Calendar.YEAR, 1)
        endMillis = endTime.timeInMillis

        // The ID of the recurring event whose instances you are searching for in the Instances table
        val selection = CalendarContract.Instances.TITLE + " = ?"
        val selectionArgs = mutableListOf<String>()
        selectionArgs.add("${reservation.name} foglalás")

        // Construct the query with the desired date range.
        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        // Submit the query
        val contentResolver = PaintballApplication.context.contentResolver
        return contentResolver.query(
            builder.build(),
            INSTANCE_PROJECTION.toTypedArray(),
            selection,
            selectionArgs.toTypedArray(),
            null
        )
    }
}