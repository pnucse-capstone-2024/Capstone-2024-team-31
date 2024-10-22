package com.wasin.presentation._util

import com.wasin.presentation.R


fun getWifi(level: Int, isSystemExist: Boolean): Int {
    return when (level) {
        4 -> if (isSystemExist) R.drawable.wifi4_pink else R.drawable.wifi1
        3 -> if (isSystemExist) R.drawable.wifi3_pink else R.drawable.wifi2
        2 -> if (isSystemExist) R.drawable.wifi2_pink else R.drawable.wifi3
        else -> if (isSystemExist) R.drawable.wifi1_pink else R.drawable.wifi4
    }
}

fun getWifiLevel(level: Long): Long {
    if (level >= -50) {
        return 4
    }
    if (level >= -70) {
        return 3
    }
    if (level >= -80) {
        return 2
    }
    return 1
}
