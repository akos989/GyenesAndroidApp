package hu.gyenes.paintball.app.db

import androidx.room.TypeConverter
import hu.gyenes.paintball.app.model.enums.ServerSyncState
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromServerSyncStateToString(value: ServerSyncState): String {
        return value.name
    }

    @TypeConverter
    fun fromStringToServerStringState(syncState: String): ServerSyncState {
        return ServerSyncState.valueOf(syncState)
    }
}