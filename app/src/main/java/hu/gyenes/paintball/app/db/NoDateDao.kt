package hu.gyenes.paintball.app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.NoDate

@Dao
interface NoDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noDates: List<NoDate>)

    @Query("SELECT * FROM no_date")
    suspend fun getAll(): List<NoDate>
}