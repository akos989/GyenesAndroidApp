package hu.gyenes.paintball.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "no_date")
data class NoDate(
    @PrimaryKey(autoGenerate = false)
    var noDateId: String,
    val reason: String,
    val fromDate: Date,
    val toDate: Date
)