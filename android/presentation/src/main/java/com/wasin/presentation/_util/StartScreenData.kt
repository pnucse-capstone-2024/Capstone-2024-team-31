package com.wasin.presentation._util

import android.content.Context
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.presentation._navigate.WasinScreen

fun startScreenData(context: Context): Boolean {
    val data = WasinDataStore(context).getData(DataStoreKey.START_SCREEN_KEY.name)
    return data == WasinScreen.LockConfirmScreen.route ||
            data == WasinScreen.MonitoringScreen.route ||
            data == WasinScreen.BackOfficeScreen.route
}
