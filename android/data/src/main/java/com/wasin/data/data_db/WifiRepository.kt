package com.wasin.data.data_db

import com.wasin.data.model.handoff.Wifi
import kotlinx.coroutines.flow.Flow

interface WifiRepository {
    fun getWifiList(): Flow<List<Wifi>>
    suspend fun deleteAll()
    suspend fun insertAll(wifi: List<Wifi>)
}
