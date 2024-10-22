package com.wasin.presentation.backoffice

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.backoffice.AcceptRequest
import com.wasin.data.model.backoffice.FindWaitingListResponse
import com.wasin.domain.usecase.backoffice.AcceptAdmin
import com.wasin.domain.usecase.backoffice.FindWaitingList
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackOfficeViewModel @Inject constructor(
    private val findWaitingListUseCase: FindWaitingList,
    private val acceptAdminUseCase: AcceptAdmin,
): ViewModel() {

    private val _waitingList = mutableStateOf(FindWaitingListResponse(emptyList()))
    val waitingList = _waitingList

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findWaitingList()
    }

    fun acceptAdmin(userId: Long, name: String) {
        viewModelScope.launch {
            acceptAdminUseCase(AcceptRequest(userId)).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _waitingList.value = _waitingList.value.copy(
                            waitingList = waitingList.value.waitingList.filter {
                                it.userId != userId
                            }
                        )
                        _eventFlow.emit(WasinEvent.MakeToast("$name 님을 승인 확인했습니다."))
                    }
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }

    fun findWaitingList() {
        viewModelScope.launch {
            findWaitingListUseCase().collect { response ->
                when (response) {
                    is Resource.Success -> _waitingList.value = response.data ?: FindWaitingListResponse(emptyList())
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }

}
