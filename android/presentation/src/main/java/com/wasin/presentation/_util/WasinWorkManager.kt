package com.wasin.presentation._util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffRequest
import com.wasin.data.model.handoff.FindBestHandOffResponse
import com.wasin.domain.usecase.handoff.FindBestHandOff
import com.wasin.domain.usecase.wifi.ConnectWifi
import com.wasin.domain.usecase.wifi.GetWifiState
import com.wasin.domain.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WasinWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStore: WasinDataStore,
    private val connectWifiUseCase: ConnectWifi,
    private val getWifiStateUseCase: GetWifiState,
    private val findBestHandOffUseCase: FindBestHandOff,
): CoroutineWorker(appContext, workerParams) {

    private val wifiRequest = mutableStateOf(FindBestHandOffRequest())
    private val wifiResponse = mutableStateOf(FindBestHandOffResponse())
    private val currentSSID = mutableStateOf("")
    private val isFail = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        // 와이파이 스캔으로 현재 안드로이드 내에 연결 가능한 와이파이 리스트 저장
        Log.d("wasin_tag", "스캔 시작")
        getLocalWifiList()
        
        if (isFail.value) {
            Log.d("wasin_tag", "스캔 실패")
            return Result.failure()
        }

        // 서버에 접속해서 상태까지 불러옴
        getDBWithStateWifiList()
        Log.d("wasin_tag", "DB 목록 조회")

        if (isFail.value || !wifiResponse.value.isAuto
            || currentSSID.value == wifiResponse.value.router.ssid) {
            Log.d("wasin_tag", "DB 목록 조회 실패")
            return Result.failure()
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Android 9 이하에서는 직접 연결
            // 관리자가 등록했던 패스워드 연결 시도 (1트)
            connectInReal()
        }
        else {
            // Android 10 이상에서는 알림 보내서 열도록 변경
            // 1트에서 실패했다면 dataStore에 저장된 패스워드로 시도 (2트)
            Log.d("wasin_tag", "패스워드 설정")
            sendNotification(wifiResponse.value.router.ssid)
            Log.d("wasin_tag", "패스워드 설정")

        }

        if (isFail.value) return Result.failure()
        return Result.success()
    }

    private suspend fun connectInReal() {
        val ssid = wifiResponse.value.router.ssid
        val password = wifiResponse.value.router.password
        connectWifiUseCase(ssid, password).collect { response ->
            when (response) {
                is Resource.Error -> connectInRealWithDataStorePassword(ssid)
                else -> {}
            }
        }
    }

    private suspend fun connectInRealWithDataStorePassword(ssid: String) {
        val password = dataStore.getData(ssid)
        connectWifiUseCase(ssid, password).collect { response ->
            when (response) {
                is Resource.Error -> isFail.value = true
                else -> {}
            }
        }
    }

    private suspend fun getLocalWifiList() {
        getWifiStateUseCase().collect { response ->
            when (response) {
                is Resource.Success -> {
                    wifiRequest.value = wifiRequest.value.copy(
                        router = response.data?.second?.router?.map { routerDTO(it) } ?: emptyList()
                    )
                    currentSSID.value = response.data?.first?.removeSurrounding("\"", "\"") ?: ""
                }
                is Resource.Error -> isFail.value = true
                else -> {}
            }
        }
    }

    private fun routerDTO(it: FindAllHandOffRequest.RouterDTO) =
        FindBestHandOffRequest.RouterDTO(
            it.ssid.ifEmpty { "알 수 없는 SSID" },
            it.macAddress,
            getWifiLevel(it.level),
            it.level
        )

    private suspend fun getDBWithStateWifiList() {
        findBestHandOffUseCase(wifiRequest.value).collect { response ->
            when (response) {
                is Resource.Success -> wifiResponse.value = response.data ?: FindBestHandOffResponse()
                is Resource.Error -> isFail.value = true
                else -> { }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(body: String) {
        // 클릭시 와이파이 설정 화면 열도록 설정
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // 알림 만들기
        val channelId = "wasin channel id"
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(com.wasin.presentation.R.drawable.wasin_icon)
                .setContentTitle("와신상담 - 최적 와이파이 변경 알림")
                .setContentText("현재 와이파이를 $body 로 변경하시겠나요?")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        // 알림 보내기
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "wasin channel name", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }
}
