package com.wasin.presentation.company_super_admin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.company.FindAllCompanyByOpenAPIRequest
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.data.model.company.SaveCompanyByOpenAPIRequest
import com.wasin.domain.usecase.company.FindAllCompanyByOpenAPI
import com.wasin.domain.usecase.company.SaveCompanyByOpenAPI
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CompanySuperAdminViewModel @Inject constructor(
    private val dataStore: WasinDataStore,
    private val findAllCompanyUseCase: FindAllCompanyByOpenAPI,
    private val saveCompanyUseCase: SaveCompanyByOpenAPI
): ViewModel() {

    private val _companyDTO = mutableStateOf(SaveCompanyByOpenAPIRequest())
    val companyDTO = _companyDTO

    private val _companyList = mutableStateOf(FindAllCompanyByOpenAPIResponse(emptyList()))
    val companyList = _companyList

    private val image = mutableStateOf<File?>(null)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun saveScreenState(nextScreen: String) {
        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, nextScreen)
    }

    fun onEvent(event: CompanySuperAdminEvent) {
        when (event) {
            is CompanySuperAdminEvent.EnterCompany -> {
                if (companyDTO.value.companyName.isNotBlank()) {
                    initCompanyDTO()
                }
                _companyDTO.value = _companyDTO.value.copy(
                    companyName = event.name
                )
            }
            is CompanySuperAdminEvent.EnterCode -> {
                _companyDTO.value = _companyDTO.value.copy(
                    serviceKey = event.code
                )
            }
            is CompanySuperAdminEvent.EnterImage -> {
                image.value = event.file
            }
            is CompanySuperAdminEvent.SelectCompany -> {
                _companyDTO.value = _companyDTO.value.copy(
                    companyName = event.name,
                    location = event.location,
                    companyFssId = event.companyFssId
                )
            }
            is CompanySuperAdminEvent.SearchCompany -> {
                findAllCompany()
            }
            is CompanySuperAdminEvent.Save -> {
                saveCompany()
            }
        }
    }

    private fun initCompanyDTO() {
        _companyDTO.value = _companyDTO.value.copy(
            companyName = "",
            location = "",
            companyFssId = ""
        )
    }

    private fun saveCompany() {
        viewModelScope.launch {
            if (isSaveCompanyInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("입력 내용은 비어있으면 안됩니다."))
                return@launch
            }
            saveCompanyUseCase(companyDTO.value, image.value!!).collect { response ->
                when (response) {
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }
    private fun findAllCompany() {
        viewModelScope.launch {
            if (isSearchInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("회사 이름은 비어있으면 안됩니다."))
                return@launch
            }
            findAllCompanyUseCase(
                FindAllCompanyByOpenAPIRequest(companyDTO.value.companyName)
            ).collect { response ->
                when (response) {
                    is Resource.Success -> _companyList.value = response.data ?: FindAllCompanyByOpenAPIResponse(emptyList())
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }

    private fun isSearchInvalid() = companyDTO.value.companyName.isEmpty()

    private fun isSaveCompanyInvalid() = companyDTO.value.companyFssId.isEmpty() || companyDTO.value.companyName.isEmpty()
            || companyDTO.value.location.isEmpty() || companyDTO.value.serviceKey.isEmpty() || image.value == null
}
