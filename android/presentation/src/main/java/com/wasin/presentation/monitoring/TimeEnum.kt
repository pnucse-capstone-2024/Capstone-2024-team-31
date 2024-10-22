package com.wasin.presentation.monitoring

enum class TimeEnum(
    val kor: String,
    val time: Int
) {
    FIVE_MINUTES_AGO("5분 전", 5),
    THIRTY_MINUTES_AGO("30분 전", 30),
    ONE_HOUR_AGO("1시간 전", 60),
    THREE_HOUR_AGO("3시간 전", 60*3),
    ONE_DAY_AGO("하루 전", 60*24),
}
