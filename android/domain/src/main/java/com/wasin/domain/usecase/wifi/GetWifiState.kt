package com.wasin.domain.usecase.wifi

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWifiState @Inject constructor(
    @ApplicationContext val context: Context
) {
    operator fun invoke(): Flow<Resource<Pair<String, FindAllHandOffRequest>>> = flow {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val success = wifiManager.startScan()
            if (success) {
                val result = FindAllHandOffRequest(
                    wifiManager.scanResults.toMutableList().map {
                        FindAllHandOffRequest.RouterDTO(it.SSID, it.BSSID, it.level.toLong(), it.level.toLong())
                    }
                )
                val currentSSID = wifiManager.connectionInfo.ssid ?: ""
                emit(Resource.Success(Pair(currentSSID, result)))
            }
            else {
                emit(Resource.Error("단기간에 많은 요청을 하면 실패합니다."))
            }
        } catch(error: Exception) {
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
