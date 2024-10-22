package com.wasin.data.model.handoff

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wifi(
    val ssid: String,
    @PrimaryKey val macAddress: String,
    val level: Long
)
