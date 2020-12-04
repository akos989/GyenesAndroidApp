package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.DTO.NoDateResponse
import hu.gyenes.paintball.app.model.NoDate
import hu.gyenes.paintball.app.network.RetrofitInstance
import retrofit2.Response

class NoDateRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insertAll(noDates: List<NoDate>) = db.getNoDateDao().insertAll(noDates)

    fun findAll() = db.getNoDateDao().findAll()

    private suspend fun remoteFindAll() = RetrofitInstance.noDateApi.findNoDates()

    override suspend fun sync() {
        val noDates = handleFindAllResponse(remoteFindAll())
        if (noDates != null) {
            insertAll(noDates)
        }
    }

    private fun handleFindAllResponse(response: Response<NoDateResponse>): List<NoDate>? {
        if (response.isSuccessful) {
            response.body()?.let {noDateResponse ->
                return noDateResponse.noDates
            }
        }
        return null
    }
}