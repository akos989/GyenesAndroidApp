package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "reservation")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val playerNumber: Int,
    val date: Date,
    val archived: Boolean,
    val gamePackage: GamePackage,
    val notes: String = ""
)