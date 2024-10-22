package com.wasin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wasin.data.db.WifiDao
import com.wasin.data.model.handoff.Wifi

@Database(
    entities = [Wifi::class],
    version = 1
)
abstract class WifiDatabase: RoomDatabase() {

    abstract val wifiDao: WifiDao

    companion object {
        const val DATABASE_NAME = "wifi_db"
    }
}
