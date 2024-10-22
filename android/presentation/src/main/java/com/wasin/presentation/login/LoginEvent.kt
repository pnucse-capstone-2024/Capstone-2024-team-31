package com.wasin.presentation.login

sealed class LoginEvent {
    data class EnterEmail(val email: String): LoginEvent()
    data class EnterPassword(val password: String): LoginEvent()
    data object Login: LoginEvent()
}
