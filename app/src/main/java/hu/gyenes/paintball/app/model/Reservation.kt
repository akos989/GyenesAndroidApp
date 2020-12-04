package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import java.io.Serializable
import java.util.*

@Entity(tableName = "reservation")
data class Reservation(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val playerNumber: Int,
    val date: Date?,
    val packageId: String,
    var timeStamp: Date?,
    val archived: Boolean = false,
    var notes: String = "",
    var syncState: ServerSyncState = ServerSyncState.REGISTERED
) : Serializable {
    @Ignore
    var gamePackage: GamePackage? = null
}