package com.wasin.data.repository

import com.wasin.data.data_db.WifiRepository
import com.wasin.data.db.WifiDao
import com.wasin.data.model.handoff.Wifi
import kotlinx.coroutines.flow.Flow

class WifiRepositoryImpl (
    private val dao: WifiDao
): WifiRepository {
    override fun getWifiList(): Flow<List<Wifi>> {
        return dao.getWifiList()
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun insertAll(wifi: List<Wifi>) {
        dao.insertAll(*wifi.toTypedArray())
    }
}
