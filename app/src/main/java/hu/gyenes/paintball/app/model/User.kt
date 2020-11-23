package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val email: String,
    var newReservations: List<Reservation> = ArrayList()
)