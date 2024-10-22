package com.wasin.presentation.wifi_list

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.handoff.FindAllHandOffRequest
import com.wasin.data.model.handoff.FindAllHandOffResponse
import com.wasin.data.model.handoff.Wifi
import com.wasin.domain.usecase.handoff.ChangeHandOffModeAuto
import com.wasin.domain.usecase.handoff.ChangeHandOffModeManual
import com.wasin.domain.usecase.handoff.FindAllHandOff
import com.wasin.domain.usecase.wifi.ConnectWifi
import com.wasin.domain.usecase.wifi.GetWifiState
import com.wasin.domain.usecase.wifi.WifiUseCase
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import com.wasin.presentation._util.getWifiLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WifiViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val dataStore: WasinDataStore,
    private val findAllHandOffUseCase: FindAllHandOff,
    private val getWifiStateUseCase: GetWifiState,
    private val connectWifiUseCase: ConnectWifi,
    private val changeHandOffModeManualUseCase: ChangeHandOffModeManual,
    private val changeHandOffModeAutoUseCase: ChangeHandOffModeAuto,
    private val wifiUseCase: WifiUseCase,
): ViewModel() {

    val wifiSort = mutableStateOf(WifiSort.RECOMMENDATION)

    private val _wifiList = mutableStateOf(FindAllHandOffResponse())
    val wifiList = _wifiList

    private val wifiRequest = mutableStateOf(FindAllHandOffRequest())

    private val _currentSSID = mutableStateOf("")
    val currentSSID = _currentSSID

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getWifiList()
    }

    fun getWifiList() {
        getLocalWifiList()
        makeWifiRequest()
    }

    fun changeHandOffModeAuto() {
        viewModelScope.launch {
            if (wifiList.value.isAuto) {
                _eventFlow.emit(WasinEvent.MakeToast("이미 자동 상태입니다."))
            }
            changeHandOffModeAutoUseCase().collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        _wifiList.value = _wifiList.value.copy(isAuto = true)
                    }
                }
            }
        }
    }

    fun changeHandOffModeManual() {
        viewModelScope.launch {
            if (!wifiList.value.isAuto) {
                _eventFlow.emit(WasinEvent.MakeToast("이미 수동 상태입니다."))
            }
            changeHandOffModeManualUseCase().collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        _wifiList.value = _wifiList.value.copy(isAuto = false)
                    }
                }
            }
        }
    }

    fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun connectInReal(ssid: String, password: String) {
        viewModelScope.launch {
            connectWifiUseCase(ssid, password).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        _currentSSID.value = ssid
                        dataStore.setData(ssid, password)
                        _eventFlow.emit(WasinEvent.MakeToast("와이파이 연결에 성공했습니다."))
                    }
                }
            }
        }
    }

    fun getPassword(ssid: String): String {
        return dataStore.getData(ssid)
    }

    fun sort(index: Int) {
        val sortFilter = WifiSort.entries.getOrNull(index) ?: WifiSort.RECOMMENDATION
        _wifiList.value = _wifiList.value.copy(
            routerList = _wifiList.value.routerList.sortedByDescending{
                if (sortFilter == WifiSort.RECOMMENDATION) {
                    it.score
                }
                else it.detailLevel
            }
        )
        wifiSort.value = sortFilter
    }

    private fun getLocalWifiList() {
        viewModelScope.launch {
            getWifiStateUseCase().collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        wifiRequest.value = wifiRequest.value.copy(
                            response.data?.second?.router?.map {
                                FindAllHandOffRequest.RouterDTO(
                                    it.ssid.ifEmpty { "알 수 없는 SSID" },
                                    it.macAddress,
                                    getWifiLevel(it.level),
                                    it.detailLevel
                                )
                            } ?: emptyList()
                        )
                        _currentSSID.value = response.data?.first?.removeSurrounding("\"", "\"") ?: ""
                    }
                }
            }
        }
    }

    private fun makeWifiRequest() {
        // 비어있으면 로컬 DB에서 가져오고
        // 들어있으면 로컬 DB에 저장
        if (wifiRequest.value.router.isEmpty()) {
            getWifiLocalDB()
        }
        else {
            saveWifiLocalDB()
            getDBWithStateWifiList()
        }
    }

    private fun saveWifiLocalDB() {
        viewModelScope.launch {
            wifiUseCase.deleteAllWifi()
            val request = wifiRequest.value.router.map {
                Wifi(it.ssid, it.macAddress, it.level)
            }
            wifiUseCase.insertAllWifi(request)
        }
    }

    private fun getWifiLocalDB() {
        viewModelScope.launch {
            wifiUseCase.getWifiList().collect { wifi ->
                wifiRequest.value = wifiRequest.value.copy(
                    router = wifi.map {
                        FindAllHandOffRequest.RouterDTO(it.ssid, it.macAddress, it.level, it.level)
                    }
                )
                getDBWithStateWifiList()
            }
        }
    }

    private fun getDBWithStateWifiList() {
        viewModelScope.launch {
            findAllHandOffUseCase(wifiRequest.value).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> _wifiList.value = response.data ?: FindAllHandOffResponse()
                }
            }
        }
    }

}
