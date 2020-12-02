package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.NoDate
import java.util.*

class NoDateRepository(val db: GyenesPaintballDatabase) : Synchronizable {
    suspend fun insertAll(noDates: List<NoDate>) = db.getNoDateDao().insertAll(noDates)

    fun findAll() = db.getNoDateDao().findAll()

    private suspend fun remoteFindAll(): List<NoDate> {
        val list: MutableList<NoDate> = mutableListOf()
        list.add(NoDate(1.toString(), "Betegség", Date(), Date()))
        list.add(NoDate(2.toString(), "Utazás", Date(), Date()))
        return list
    }

    override suspend fun sync() {
        val noDates = remoteFindAll()
        insertAll(noDates)
    }
}