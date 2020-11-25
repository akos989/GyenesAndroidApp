package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.GamePackage

class GamePackageRepository(val db: GyenesPaintballDatabase) {
    suspend fun insertAll(gamePackages: List<GamePackage>) = db.getGamePackageDao().insertAll(gamePackages)

    suspend fun findAll(): List<GamePackage> = db.getGamePackageDao().findAll()
}