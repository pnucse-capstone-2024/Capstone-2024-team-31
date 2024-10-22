package com.wasin.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.user.LoginRequest
import com.wasin.domain.usecase.user.Login
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: WasinDataStore,
    private val loginUseCase: Login,
): ViewModel() {

    private val _loginDTO = mutableStateOf(LoginRequest())
    val loginDTO = _loginDTO

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnterEmail -> {
                _loginDTO.value = _loginDTO.value.copy(
                    email = event.email
                )
            }
            is LoginEvent.EnterPassword -> {
                _loginDTO.value = _loginDTO.value.copy(
                    password = event.password
                )
            }
            is LoginEvent.Login -> {
                _loginDTO.value = _loginDTO.value.copy(
                    fcmToken = getFcmToken()
                )
                login()
            }
        }
    }

    fun saveScreenState(nextScreen: String) {
        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, nextScreen)
    }

    private fun login() {
        viewModelScope.launch {
            if (isInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("이메일이나 비밀번호는 비어있으면 안됩니다."))
                return@launch
            }
            if (loginDTO.value.fcmToken.isEmpty()) {
                _eventFlow.emit(WasinEvent.MakeToast("잠시 후 다시 시도해주세요."))
                return@launch
            }
            loginUseCase(loginDTO.value).collect { response ->
                when(response){
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        dataStore.setData(DataStoreKey.EMAIL_KEY.name, loginDTO.value.email)
                        _eventFlow.emit(WasinEvent.Navigate)
                    }
                }
            }
        }
    }

    private fun getFcmToken() = dataStore.getData(DataStoreKey.FCM_TOKEN_KEY.name)

    private fun isInvalid() = loginDTO.value.email.isEmpty() || loginDTO.value.password.isEmpty()

}
