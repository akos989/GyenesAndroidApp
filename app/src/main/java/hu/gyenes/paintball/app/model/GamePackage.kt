package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "package")
data class GamePackage(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val fromNumberLimit: Int,
    val toNumberLimit: Int,
    val bulletPrice: Int,
    val basePrice: Int,
    val duration: Int,
    val disabled: Boolean,
    val includedBullets: Int,
    val description: String = ""
)