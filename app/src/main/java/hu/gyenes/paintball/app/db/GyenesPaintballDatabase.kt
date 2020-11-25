package hu.gyenes.paintball.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.gyenes.paintball.app.model.GamePackage
import hu.gyenes.paintball.app.model.NoDate
import hu.gyenes.paintball.app.model.Reservation
import hu.gyenes.paintball.app.model.User

@Database(
    entities = [
        Reservation::class, GamePackage::class,
        NoDate::class, User::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GyenesPaintballDatabase : RoomDatabase() {

    abstract fun getReservationDao(): ReservationDao
    abstract fun getNoDateDao(): NoDateDao
    abstract fun getGamePackageDao(): GamePackageDao
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile //changes are applied to all threads
        private var instance: GyenesPaintballDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GyenesPaintballDatabase::class.java,
                "article_db.db"
            ).build()
    }
}