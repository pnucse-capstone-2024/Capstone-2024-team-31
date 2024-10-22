package com.wasin.presentation.signup

sealed class SignupEvent {
    data class EnterName(val name: String): SignupEvent()
    data class EnterEmail(val email: String): SignupEvent()
    data class EnterEmailCode(val code: String): SignupEvent()
    data class EnterPassword(val password: String): SignupEvent()
    data class EnterPasswordConfirm(val passwordConfirm: String): SignupEvent()
    data class Signup(val role: String): SignupEvent()
    data object SendEmail: SignupEvent()
    data object CheckEmail: SignupEvent()
}
