package com.wasin.domain.usecase.wifi

import com.wasin.data.data_db.WifiRepository
import com.wasin.data.model.handoff.Wifi

class InsertAllWifi (
    private val repository: WifiRepository
) {
    suspend operator fun invoke(wifi: List<Wifi>) {
        repository.insertAll(wifi)
    }
}
