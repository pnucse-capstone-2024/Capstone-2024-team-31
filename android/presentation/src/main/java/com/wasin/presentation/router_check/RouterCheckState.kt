package com.wasin.presentation.router_check

data class RouterCheckState(
    val isLoading: Boolean = false,
    val result: String = "",
    val error: String = ""
)
