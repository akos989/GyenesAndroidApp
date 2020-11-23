package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reservation")
data class Reservation(
    @PrimaryKey(autoGenerate = false)
    val reservationId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val playerNumber: Int,
    val date: Date,
    val archived: Boolean,
    val packageId: String,
    val notes: String = ""
)