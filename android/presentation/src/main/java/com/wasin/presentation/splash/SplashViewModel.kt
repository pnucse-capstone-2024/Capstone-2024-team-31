package com.wasin.presentation.splash

import androidx.lifecycle.ViewModel
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.presentation._navigate.WasinScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataStore: WasinDataStore
): ViewModel() {

    fun getScreen(): String {
        val screen = dataStore.getData(DataStoreKey.START_SCREEN_KEY.name)
        return screen.ifEmpty { return WasinScreen.LoginScreen.route }
    }

}
