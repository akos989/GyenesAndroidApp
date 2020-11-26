package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.NoDate

class NoDateRepository(val db: GyenesPaintballDatabase) {
    suspend fun insertAll(noDates: List<NoDate>) = db.getNoDateDao().insertAll(noDates)

    fun findAll() = db.getNoDateDao().findAll()
}