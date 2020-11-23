package hu.gyenes.paintball.app.model.relations

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "reservationId"])
data class UserNotViewedReservationCrossRef(
    val userId: String,
    val reservationId: String
)