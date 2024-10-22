package com.wasin.presentation.router_log

data class RouterLogState(
    val isLoading: Boolean = false,
    val log: String = "",
    val error: String = ""
)
