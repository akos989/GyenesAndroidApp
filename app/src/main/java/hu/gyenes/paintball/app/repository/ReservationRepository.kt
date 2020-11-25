package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.Reservation

class ReservationRepository(val db: GyenesPaintballDatabase) {
    suspend fun insert(reservation: Reservation) = db.getReservationDao().insert(reservation)

    suspend fun insertAll(reservations: List<Reservation>) = db.getReservationDao().insertAll(reservations)

    suspend fun delete(reservation: Reservation) = db.getReservationDao().delete(reservation)

    suspend fun update(reservation: Reservation) = db.getReservationDao().update(reservation)

    fun findAll() = db.getReservationDao().findAll()
}