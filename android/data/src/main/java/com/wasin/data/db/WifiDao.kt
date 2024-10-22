package com.wasin.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wasin.data.model.handoff.Wifi
import kotlinx.coroutines.flow.Flow

@Dao
interface WifiDao {
    @Query("SELECT * FROM wifi")
    fun getWifiList(): Flow<List<Wifi>>

    @Query("DELETE FROM wifi")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg wifiList: Wifi)
}
