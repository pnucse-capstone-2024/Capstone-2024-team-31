package com.wasin.domain.usecase.wifi

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.wasin.data.util.getErrorMessage
import com.wasin.domain.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConnectWifi @Inject constructor(
    @ApplicationContext val context: Context
) {
    operator fun invoke(ssid: String, password: String): Flow<Resource<String>> = flow {
        try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager;
            val wifiConfig = WifiConfiguration().apply {
                this.SSID = "\"$ssid\""
                this.preSharedKey = "\"$password\""
                this.priority = 999999
            }

            val networkId = wifiManager.addNetwork(wifiConfig)
            if (networkId == -1) {
                emit(Resource.Error("네트워크 추가에 실패했습니다. - 1"))
            }

            wifiManager.disconnect()
            val isEnabled = wifiManager.enableNetwork(networkId, true)
            wifiManager.reconnect()

            if (isEnabled) {
                emit(Resource.Success("네트워크 추가에 성공했습니다."))
            }
            else emit(Resource.Error("네트워크 추가에 실패했습니다."))

        } catch(error: Exception) {
            emit(Resource.Error(getErrorMessage(error)))
        }
    }
}
