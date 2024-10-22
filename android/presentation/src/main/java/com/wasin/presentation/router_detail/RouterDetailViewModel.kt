package com.wasin.presentation.router_detail

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.router.FindByRouterIdResponse
import com.wasin.data.model.router.ImageSize
import com.wasin.data.model.router.RouterPosition
import com.wasin.domain.usecase.router.DeleteRouter
import com.wasin.domain.usecase.router.FindRouterById
import com.wasin.domain.utils.ImagePositionMapper
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterDetailViewModel @Inject constructor (
    private val findRouterByIdUseCase: FindRouterById,
    private val deleteRouterUseCase: DeleteRouter,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _routerDTO = mutableStateOf(FindByRouterIdResponse())
    val routerDTO = _routerDTO

    private val _imageWidth = mutableStateOf(0)
    private val _routerId = mutableLongStateOf(-1L)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Long>("routerId")?.let{
            _routerId.longValue = it
        }
        findRouterById()
    }

    fun enterImageSize(width: Int) {
        _imageWidth.value = width
    }

    fun refresh() {
        findRouterById()
    }
    private fun findRouterById() {
        viewModelScope.launch {
            findRouterByIdUseCase(_routerId.longValue).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        _routerDTO.value = response.data ?: FindByRouterIdResponse()
                        mappingPosition()
                    }
                }
            }
        }
    }
    private fun mappingPosition() {
        val image = routerDTO.value.image
        val router = routerDTO.value.information
        val position = ImagePositionMapper.positionServerToLocal(
            serverPosition = RouterPosition(router.positionX, router.positionY),
            serverImageSize = ImageSize(image.imageWidth, image.imageHeight),
            localImageWidth = _imageWidth.value
        )

        _routerDTO.value = _routerDTO.value.copy(
            information = FindByRouterIdResponse.RouterInformation(
                name = router.name,
                score = router.score,
                ssid = router.ssid,
                macAddress = router.macAddress,
                instance = router.instance,
                serialNumber = router.serialNumber,
                port = router.port,
                password = router.password,
                positionX = position?.x ?: -1.0,
                positionY = position?.y ?: -1.0,
            )
        )
    }

    fun deleteRouter() {
        viewModelScope.launch {
            deleteRouterUseCase(_routerId.longValue).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> {
                        _eventFlow.emit(WasinEvent.MakeToast("라우터가 삭제되었습니다."))
                        _eventFlow.emit(WasinEvent.Navigate)
                    }
                }
            }
        }
    }

}
