package com.wasin.domain.usecase.wifi

import com.wasin.data.data_db.WifiRepository

class DeleteAllWifi (
    private val repository: WifiRepository
) {
    suspend operator fun invoke() {
        return repository.deleteAll()
    }
}
