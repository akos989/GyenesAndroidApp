package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.DTO.GamePackageResponse
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.network.RetrofitInstance
import retrofit2.Response

class GamePackageRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insertAll(gamePackages: List<GamePackage>) = db.getGamePackageDao().insertAll(gamePackages)

    fun localFindAll() = db.getGamePackageDao().findAll()

    private suspend fun remoteFindAll() = RetrofitInstance.gamePackagesApi.findPackages()

    override suspend fun sync() {
        val gamePackages = handleFindAllResponse(remoteFindAll())
        if (gamePackages != null) {
            insertAll(gamePackages)
        }
    }

    private fun handleFindAllResponse(response: Response<GamePackageResponse>): List<GamePackage>? {
        if (response.isSuccessful) {
            response.body()?.let {gamePackageResponse ->
                return gamePackageResponse.packages
            }
        }
        return null
    }
}