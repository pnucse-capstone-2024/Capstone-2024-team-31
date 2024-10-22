package com.wasin.presentation.lock_setting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.user.LockRequest
import com.wasin.domain.usecase.user.Lock
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockSettingViewModel @Inject constructor(
    private val dataStore: WasinDataStore,
    private val lockUseCase: Lock
): ViewModel() {

    private val _lockDTO = mutableStateOf("")
    val lockDTO = _lockDTO

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun addPassword(num: String) {
        if (lockDTO.value.length >= 4) return
        lockDTO.value = lockDTO.value.plus(num)
        if (lockDTO.value.length == 4) lockSetting()
    }

    fun removeLast() {
        if (lockDTO.value.isNotEmpty()) {
            lockDTO.value = lockDTO.value.dropLast(1)
        }
    }

    fun saveScreenState(nextScreen: String) {
        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, nextScreen)
    }

    private fun lockSetting() {
        viewModelScope.launch {
            lockUseCase(LockRequest(lockDTO.value)).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                }
            }
        }
    }

}
