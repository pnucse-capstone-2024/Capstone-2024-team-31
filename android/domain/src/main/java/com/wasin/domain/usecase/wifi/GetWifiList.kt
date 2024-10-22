package com.wasin.domain.usecase.wifi

import com.wasin.data.data_db.WifiRepository
import com.wasin.data.model.handoff.Wifi
import kotlinx.coroutines.flow.Flow

class GetWifiList (
    private val repository: WifiRepository
) {
    operator fun invoke(): Flow<List<Wifi>> {
        return repository.getWifiList()
    }
}
