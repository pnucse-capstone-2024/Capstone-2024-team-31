package com.wasin.presentation.router_list

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.router.FindAllRouterResponse
import com.wasin.data.model.router.ImageSize
import com.wasin.data.model.router.RouterPosition
import com.wasin.domain.usecase.router.FindAllRouter
import com.wasin.domain.utils.ImagePositionMapper
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouterListViewModel @Inject constructor(
    private val findAllRouterUseCase: FindAllRouter
): ViewModel() {

    private val _routerListDTO = mutableStateOf(FindAllRouterResponse())
    val routerListDTO = _routerListDTO

    private val _imageWidth = mutableStateOf(0)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findAllRouter()
    }

    fun enterImageSize(width: Int) {
        _imageWidth.value = width
    }

    fun refresh() {
        findAllRouter()
    }

    private fun findAllRouter() {
        viewModelScope.launch {
            findAllRouterUseCase().collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        _routerListDTO.value = response.data ?: FindAllRouterResponse()
                        mappingPosition()
                    }
                }
            }
        }
    }

    private fun mappingPosition() {
        val image = routerListDTO.value.image
        _routerListDTO.value = _routerListDTO.value.copy(
            routerList = routerListDTO.value.routerList.map {
                val position = ImagePositionMapper.positionServerToLocal(
                    serverPosition = RouterPosition(it.positionX, it.positionY),
                    serverImageSize = ImageSize(image.imageWidth, image.imageHeight),
                    localImageWidth = _imageWidth.value
                )
                FindAllRouterResponse.EachRouter(
                    routerId = it.routerId,
                    name = it.name,
                    score = it.score,
                    positionX = position?.x ?: -1.0,
                    positionY = position?.y ?: -1.0
                )
            }
        )
    }
}
