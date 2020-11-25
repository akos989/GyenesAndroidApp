package hu.gyenes.paintball.app.db

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.Reservation

@Dao
interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(reservation: Reservation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservations: List<Reservation>)

    @Delete
    suspend fun delete(reservation: Reservation)

    @Update
    suspend fun update(reservation: Reservation)

    @Query("SELECT * FROM reservation")
    fun findAll(): LiveData<List<Reservation>>
}