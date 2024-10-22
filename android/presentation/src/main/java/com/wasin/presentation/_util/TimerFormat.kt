package com.wasin.presentation._util

import java.text.DecimalFormat

fun Long.timerFormat(): String {
    val decimalFormat = DecimalFormat("00")
    val hour = (this/1000) / 60
    val minute = (this/1000) % 60
    return decimalFormat.format(hour) + ":" + decimalFormat.format(minute)
}
