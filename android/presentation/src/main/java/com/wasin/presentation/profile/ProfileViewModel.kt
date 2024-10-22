package com.wasin.presentation.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.domain.usecase.profile.ChangeModeAuto
import com.wasin.domain.usecase.profile.ChangeModeManual
import com.wasin.domain.usecase.profile.ChangeProfile
import com.wasin.domain.usecase.profile.FindAllProfile
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val findAllProfileUseCase: FindAllProfile,
    private val changeModeAutoUseCase: ChangeModeAuto,
    private val changeModeManualUseCase: ChangeModeManual,
    private val changeProfileUseCase: ChangeProfile
): ViewModel() {

    private val _profileDTO = mutableStateOf(FindAllProfileResponse())
    val profileDTO = _profileDTO

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val isLoading = mutableStateOf(false)

    init {
        findAllProfile()
    }

    fun changeProfile(profileId: Long) {
        viewModelScope.launch {
            if (profileId == profileDTO.value.activeProfileId) {
                _eventFlow.emit(WasinEvent.MakeToast("이미 해당 프로파일은 선택되었습니다."))
                return@launch
            }
            changeProfileUseCase(profileId).collect { response ->
                isLoading.value = response is Resource.Loading
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        _profileDTO.value = _profileDTO.value.copy(
                            activeProfileId = profileId
                        )
                    }
                }
            }
        }
    }

    fun changeModeAuto() {
        viewModelScope.launch {
            if (profileDTO.value.isAuto) {
                _eventFlow.emit(WasinEvent.MakeToast("이미 자동 모드입니다."))
                return@launch
            }
            changeModeAutoUseCase().collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        _profileDTO.value = _profileDTO.value.copy(
                            isAuto = true
                        )
                    }
                }
            }
        }
    }

    fun changeModeManual() {
        viewModelScope.launch {
            if (!profileDTO.value.isAuto) {
                _eventFlow.emit(WasinEvent.MakeToast("이미 수동 모드입니다."))
                return@launch
            }
            changeModeManualUseCase().collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        _profileDTO.value = _profileDTO.value.copy(
                            isAuto = false
                        )
                    }
                }
            }
        }
    }

    fun findAllProfile() {
        viewModelScope.launch {
            findAllProfileUseCase().collect { response ->
                when (response) {
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> _profileDTO.value = response.data ?: FindAllProfileResponse()
                }
            }
        }
    }
}
