package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.DTO.ReservationDeleteRequest
import hu.gyenes.paintball.app.model.DTO.ReservationResponse
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import hu.gyenes.paintball.app.network.RetrofitInstance
import retrofit2.Response
import java.util.*

class ReservationRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insert(reservation: Reservation) {
        db.getReservationDao().insert(reservation)
    }

    suspend fun insertRemote(reservation: Reservation)
            = RetrofitInstance.reservationApi.insert(reservation)

    suspend fun update(reservation: Reservation) {
        db.getReservationDao().update(reservation)
    }

    suspend fun updateRemote(id: String, reservation: Reservation)
            = RetrofitInstance.reservationApi.update(id, reservation)

    suspend fun delete(reservation: Reservation) {
        db.getReservationDao().delete(reservation)
    }

    suspend fun deleteRemote(ids: List<String>) = RetrofitInstance.reservationApi.delete(
        ReservationDeleteRequest(ids)
    )

    fun findAll() = db.getReservationDao().findAll()

    private suspend fun remoteFindAll() = RetrofitInstance.reservationApi.findReservations()

    private suspend fun insertAll(reservations: List<Reservation>) = db.getReservationDao().insertAll(reservations)

    override suspend fun sync() {
        syncPendingReservations()
        val reservations = handleFindAllResponse(remoteFindAll())
        if (reservations != null) {
            insertAll(reservations)
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
                else -> {}
            }
        }
        if (toDeleteReservation.size > 0) {
            val response = deleteRemote(toDeleteReservation.map { r -> r.id })
            if (response.isSuccessful) {
                response.body()?.let {
                    db.getReservationDao().deleteAll(toDeleteReservation)
                }
            }
        }
    }

    private fun handleFindAllResponse(response: Response<ReservationResponse>): List<Reservation>? {
        if (response.isSuccessful) {
            response.body()?.let {reservationResponse ->
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
}