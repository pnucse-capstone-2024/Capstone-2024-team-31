package com.wasin.presentation.router_add

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.router.CreateRouterRequest
import com.wasin.data.model.router.FindCompanyImageResponse
import com.wasin.data.model.router.ImageSize
import com.wasin.data.model.router.RouterPosition
import com.wasin.domain.usecase.router.CreateRouter
import com.wasin.domain.usecase.router.FindCompanyImage
import com.wasin.domain.utils.ImagePositionMapper
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterAddViewModel @Inject constructor(
    private val findCompanyImageUseCase: FindCompanyImage,
    private val createRouterUseCase: CreateRouter
): ViewModel() {

    private val _routerDTO = mutableStateOf(CreateRouterRequest())
    val routerDTO = _routerDTO

    private val _imageDTO = mutableStateOf(FindCompanyImageResponse())
    val imageDTO = _imageDTO

    private val _position = mutableStateOf<RouterPosition?>(null)
    val position = _position

    private val _imageWidth = mutableStateOf(0)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findCompanyImage()
    }

    fun onEvent(event: RouterAddEvent) {
        when (event) {
            is RouterAddEvent.EnterMacAddress -> {
                _routerDTO.value = _routerDTO.value.copy(
                    macAddress = event.macAddress
                )
            }
            is RouterAddEvent.EnterName -> {
                _routerDTO.value = _routerDTO.value.copy(
                    name = event.name
                )
            }
            is RouterAddEvent.EnterSerialNumber -> {
                _routerDTO.value = _routerDTO.value.copy(
                    serialNumber = event.serialNumber
                )
            }
            is RouterAddEvent.EnterPassword -> {
                _routerDTO.value = _routerDTO.value.copy(
                    password = event.password
                )
            }
            is RouterAddEvent.EnterPort -> {
                _routerDTO.value = _routerDTO.value.copy(
                    port = event.port
                )
            }
            is RouterAddEvent.EnterPosition -> {
                _position.value = RouterPosition(event.x.toDouble(), event.y.toDouble())
            }
            is RouterAddEvent.EnterImageSize -> {
                _imageWidth.value = event.width
            }
            is RouterAddEvent.SaveRouter -> {
                if (position.value == null) {
                    viewModelScope.launch {
                        _eventFlow.emit(WasinEvent.MakeToast("마커를 클릭해주세요."))
                    }
                    return
                }
                val serverPosition = getServerPosition()
                if (serverPosition != null) {
                    _routerDTO.value = _routerDTO.value.copy(
                        positionX = serverPosition.x,
                        positionY = serverPosition.y
                    )
                    createRouter()
                }
            }
        }
    }

    private fun getServerPosition(): RouterPosition? {
        return ImagePositionMapper.positionLocalToServer(
            localPosition = position.value!!,
            serverImageSize = ImageSize(imageDTO.value.imageWidth, imageDTO.value.imageHeight),
            localImageWidth =_imageWidth.value
        )
    }

    private fun findCompanyImage() {
        viewModelScope.launch {
            findCompanyImageUseCase().collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> _imageDTO.value = response.data ?: FindCompanyImageResponse()
                }
            }
        }
    }

    private fun createRouter() {
        viewModelScope.launch {
            if (isInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("입력 내용은 비어있으면 안됩니다."))
                return@launch
            }
            createRouterUseCase(routerDTO.value).collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                }
            }
        }
    }

    private fun isInvalid() = routerDTO.value.name.isEmpty() || routerDTO.value.macAddress.isEmpty()
            || routerDTO.value.serialNumber.isEmpty() || routerDTO.value.password.isEmpty() || routerDTO.value.port.isEmpty()

}
