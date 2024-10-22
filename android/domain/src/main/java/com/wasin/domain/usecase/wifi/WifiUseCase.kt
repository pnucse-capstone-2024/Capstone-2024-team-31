package com.wasin.domain.usecase.wifi

data class WifiUseCase(
    val getWifiList: GetWifiList,
    val deleteAllWifi: DeleteAllWifi,
    val insertAllWifi: InsertAllWifi
)
