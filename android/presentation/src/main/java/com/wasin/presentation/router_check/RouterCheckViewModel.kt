package com.wasin.presentation.router_check

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.domain.usecase.router.CheckRouter
import com.wasin.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RouterCheckViewModel @Inject constructor(
    private val routerCheckRouter: CheckRouter,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val routerId = mutableLongStateOf(-1L)

    private val _result = mutableStateOf(RouterCheckState())
    val result = _result

    init {
        savedStateHandle.get<Long>("routerId")?.let{
            routerId.longValue = it
        }
        checkRouter()
    }

    private fun checkRouter() {
        routerCheckRouter(routerId.longValue).onEach { response ->
            _result.value = _result.value.copy(
                result = response.data?.result ?: "",
                isLoading = response is Resource.Loading,
                error = if (response is Resource.Error) response.message else ""
            )
        }.launchIn(viewModelScope)
    }
}
