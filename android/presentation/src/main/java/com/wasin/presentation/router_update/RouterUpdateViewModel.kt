package com.wasin.presentation.router_update

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.router.FindByRouterIdResponse
import com.wasin.data.model.router.ImageSize
import com.wasin.data.model.router.RouterPosition
import com.wasin.data.model.router.UpdateRouterRequest
import com.wasin.domain.usecase.router.FindRouterById
import com.wasin.domain.usecase.router.UpdateRouter
import com.wasin.domain.utils.ImagePositionMapper
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterUpdateViewModel @Inject constructor(
    private val findRouterByIdUseCase: FindRouterById,
    private val updateRouterUseCase: UpdateRouter,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _requestDTO = mutableStateOf(UpdateRouterRequest())
    val requestDTO = _requestDTO

    private val _routerDTO = mutableStateOf(FindByRouterIdResponse())
    val routerDTO = _routerDTO

    private val _position = mutableStateOf<RouterPosition?>(null)
    val position = _position

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

    fun onEvent(event: RouterUpdateEvent) {
        when (event) {
            is RouterUpdateEvent.EnterName -> {
                _requestDTO.value = _requestDTO.value.copy(
                    name = event.name
                )
            }
            is RouterUpdateEvent.EnterPassword -> {
                _requestDTO.value = _requestDTO.value.copy(
                    password = event.password
                )
            }
            is RouterUpdateEvent.EnterPosition -> {
                _position.value = RouterPosition(event.x.toDouble(), event.y.toDouble())
            }
            is RouterUpdateEvent.EnterImageSize -> {
                _imageWidth.value = event.width
            }
            is RouterUpdateEvent.UpdateRouter -> {
                if (position.value == null) {
                    viewModelScope.launch {
                        _eventFlow.emit(WasinEvent.MakeToast("마커를 클릭해주세요."))
                    }
                    return
                }
                val serverPosition = getServerPosition()
                if (serverPosition != null) {
                    _requestDTO.value = _requestDTO.value.copy(
                        positionX = serverPosition.x,
                        positionY = serverPosition.y
                    )
                    updateRouter()
                }
            }
        }
    }

    private fun updateRouter() {
        viewModelScope.launch {
            if (isInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("입력 내용은 비어있으면 안됩니다."))
                return@launch
            }
            updateRouterUseCase(requestDTO.value, _routerId.longValue).collect { response ->
                when (response) {
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                }
            }
        }
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
        _requestDTO.value = _requestDTO.value.copy(
            name = routerDTO.value.information.name,
            password = routerDTO.value.information.password
        )
        _position.value = RouterPosition(
            x = position?.x ?: -1.0,
            y = position?.y ?: -1.0,
        )
    }

    private fun getServerPosition(): RouterPosition? {
        return ImagePositionMapper.positionLocalToServer(
            localPosition = position.value!!,
            serverImageSize = ImageSize(routerDTO.value.image.imageWidth, routerDTO.value.image.imageHeight),
            localImageWidth =_imageWidth.value
        )
    }

    private fun isInvalid() = requestDTO.value.name.isEmpty() || requestDTO.value.password.isEmpty()

}
