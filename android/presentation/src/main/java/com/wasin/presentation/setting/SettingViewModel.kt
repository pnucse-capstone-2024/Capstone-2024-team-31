package com.wasin.presentation.setting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.domain.usecase.user.Logout
import com.wasin.domain.usecase.user.Withdraw
import com.wasin.domain.utils.Resource
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val logoutUseCase: Logout,
    private val withdrawUseCase: Withdraw,
    private val dataStore: WasinDataStore
): ViewModel() {

    private val _email = mutableStateOf("")
    val email = _email

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findEmail()
    }

    private fun findEmail() {
        _email.value = dataStore.getData(DataStoreKey.EMAIL_KEY.name)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, WasinScreen.LoginScreen.route)
                        _eventFlow.emit(WasinEvent.Navigate)
                    }
                }
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            withdrawUseCase().collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, WasinScreen.LoginScreen.route)
                        _eventFlow.emit(WasinEvent.Navigate)
                    }
                }
            }
        }
    }




}
