package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.GamePackage

class GamePackageRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insertAll(gamePackages: List<GamePackage>) = db.getGamePackageDao().insertAll(gamePackages)

    fun localFindAll() = db.getGamePackageDao().findAll()

    private suspend fun remoteFindAll(): List<GamePackage> {
        val list: MutableList<GamePackage> = mutableListOf()
        list.add(GamePackage(700.toString(), 100, 400, 15, 40000, 2, false, 0 ))
        list.add(GamePackage(100.toString(), 10, 20, 15, 3000, 2, false, 0 ))
        return list
    }

    override suspend fun sync() {
        val packages = remoteFindAll()

        insertAll(packages)
    }
}