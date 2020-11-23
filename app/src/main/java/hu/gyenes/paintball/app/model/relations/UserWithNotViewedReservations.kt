package hu.gyenes.paintball.app.model.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.User

data class UserWithNotViewedReservations(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "reservationId",
        associateBy = Junction(UserWithNotViewedReservations::class)
    )
    val notViewedReservations: List<Reservation>
)