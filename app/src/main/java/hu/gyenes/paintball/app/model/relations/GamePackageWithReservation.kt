package hu.gyenes.paintball.app.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.Reservation

data class GamePackageWithReservation(
    @Embedded val gamePackage: GamePackage,
    @Relation(
        parentColumn = "gamePackageId",
        entityColumn = "packageId"
    )
    val reservations: List<Reservation>
)