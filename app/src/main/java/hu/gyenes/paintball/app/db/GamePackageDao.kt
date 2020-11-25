package hu.gyenes.paintball.app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.gyenes.paintball.app.model.GamePackage

@Dao
interface GamePackageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(gamePackages: List<GamePackage>)

    @Query("SELECT * FROM game_package")
    suspend fun findAll(): List<GamePackage>
}