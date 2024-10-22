package com.wasin.presentation.router_log

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.router.LogEmailRequest
import com.wasin.domain.usecase.router.RouterLog
import com.wasin.domain.usecase.router.SendRouterLog
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterLogViewModel @Inject constructor(
    private val routerLog: RouterLog,
    private val sendRouterLog: SendRouterLog,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val routerId = mutableLongStateOf(-1L)

    private val _result = mutableStateOf(RouterLogState())
    val result = _result

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("routerId")?.let{
            routerId.longValue = it
        }
        findLog()
    }

    fun sendEmail() {
        viewModelScope.launch {
            sendRouterLog(
                routerId.longValue,
                LogEmailRequest(result.value.log)
            ).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> _eventFlow.emit(WasinEvent.MakeToast("이메일 전송에 성공했습니다."))
                }
            }
        }
    }

    private fun findLog() {
        routerLog(routerId.longValue).onEach { response ->
            _result.value = _result.value.copy(
                log = response.data?.log ?: "",
                isLoading = response is Resource.Loading,
                error = if (response is Resource.Error) response.message else ""
            )
        }.launchIn(viewModelScope)
    }

}
