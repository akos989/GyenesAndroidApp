package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "game_package")
data class GamePackage(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val fromNumberLimit: Int,
    val toNumberLimit: Int,
    val bulletPrice: Int,
    val basePrice: Int,
    val duration: Int,
    val disabled: Boolean,
    val includedBullets: Int,
    val description: String = ""
)