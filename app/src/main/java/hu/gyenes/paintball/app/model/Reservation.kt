package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "reservation")
data class Reservation(
    @PrimaryKey(autoGenerate = false)
    val reservationId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val playerNumber: Int,
    val date: Date?,
    val packageId: String,
    val createdAt: Date?,
    val archived: Boolean = false,
    val notes: String = ""
) : Serializable {
    @Ignore
    var gamePackage: GamePackage? = null
}