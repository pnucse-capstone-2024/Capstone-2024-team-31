package com.wasin.presentation.company_admin

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.company.FindAllCompanyByDBResponse
import com.wasin.data.model.company.SaveCompanyByDBRequest
import com.wasin.domain.usecase.company.FindAllCompanyByDB
import com.wasin.domain.usecase.company.SaveCompanyByDB
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyAdminViewModel @Inject constructor(
    private val dataStore: WasinDataStore,
    private val findAllCompanyUseCase: FindAllCompanyByDB,
    private val saveCompanyUseCase: SaveCompanyByDB
): ViewModel() {

    private val _companyList = mutableStateOf(FindAllCompanyByDBResponse(emptyList()))
    val companyList = _companyList

    private val _companyId = mutableLongStateOf(-1L)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        findAllCompany()
    }

    fun saveCompany() {
        viewModelScope.launch {
            saveCompanyUseCase(SaveCompanyByDBRequest(_companyId.longValue.toLong())).collect { response ->
                when (response) {
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }

    fun saveScreenState(nextScreen: String) {
        dataStore.setData(DataStoreKey.START_SCREEN_KEY.name, nextScreen)
    }

    fun setCompanyId(companyId: Long) {
        _companyId.longValue = companyId
    }

    fun isSelected(index: Long) = index == _companyId.longValue

    private fun findAllCompany() {
        viewModelScope.launch {
            findAllCompanyUseCase().collect { response ->
                when (response) {
                    is Resource.Success -> _companyList.value = response.data ?: FindAllCompanyByDBResponse(emptyList())
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                }
            }
        }
    }

}
