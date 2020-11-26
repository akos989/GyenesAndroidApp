package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.Reservation
import java.util.*

class ReservationRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insert(reservation: Reservation) = db.getReservationDao().insert(reservation)

    suspend fun insertAll(reservations: List<Reservation>) = db.getReservationDao().insertAll(reservations)

    suspend fun delete(reservation: Reservation) = db.getReservationDao().delete(reservation)

    suspend fun update(reservation: Reservation) = db.getReservationDao().update(reservation)

    fun findAll() = db.getReservationDao().findAll()

    private suspend fun remoteFindAll(): List<Reservation> {
        val list: MutableList<Reservation> = mutableListOf()
        list.add(Reservation(200.toString(), "Kiss János", "morvaiakos@asdlf.com", "021234123", 16, Date(), 700.toString(), Date()))
        list.add(Reservation(300.toString(), "Nagy Béla", "morvaiakos@asdlf.com", "021234123", 16, Date(), 100.toString(), Date()))
        return list
    }

    override suspend fun sync() {
        val reservations = remoteFindAll()

        insertAll(reservations)
    }
}