package com.wasin.presentation.signup

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasin.data.model.user.EmailCheckRequest
import com.wasin.data.model.user.EmailRequest
import com.wasin.data.model.user.SignupRequest
import com.wasin.domain.usecase.user.EmailCheck
import com.wasin.domain.usecase.user.EmailSend
import com.wasin.domain.usecase.user.Signup
import com.wasin.domain.utils.Resource
import com.wasin.presentation._util.WasinEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: Signup,
    private val emailSendUseCase: EmailSend,
    private val emailCheckUseCase: EmailCheck,
): ViewModel() {

    private val _signupDTO = mutableStateOf(SignupRequest())
    val signupDTO = _signupDTO

    private val _emailDTO = mutableStateOf(EmailRequest())
    val emailDTO = _emailDTO

    private val _emailCodeDTO = mutableStateOf(EmailCheckRequest())
    val emailCodeDTO = _emailCodeDTO

    val enabled = mutableStateOf(true)
    val isTimerMove = mutableStateOf(false)
    val timer = mutableLongStateOf(TIMER_LENGTH)

    private var coroutineScope = CoroutineScope(Dispatchers.Main)

    private val _eventFlow = MutableSharedFlow<WasinEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.EnterName -> {
                _signupDTO.value = _signupDTO.value.copy(
                    username = event.name
                )
            }
            is SignupEvent.EnterEmail -> {
                if (emailCodeDTO.value.email.isNotEmpty() && emailCodeDTO.value.email != event.email) {
                    emailChangedError()
                }
                _emailDTO.value = _emailDTO.value.copy(
                    email = event.email
                )
                _signupDTO.value = _signupDTO.value.copy(
                    email = event.email
                )
            }
            is SignupEvent.EnterEmailCode -> {
                if (event.code.length <= 6) {
                    _emailCodeDTO.value = _emailCodeDTO.value.copy(
                        code = event.code
                    )
                }
            }
            is SignupEvent.EnterPassword -> {
                _signupDTO.value = _signupDTO.value.copy(
                    password = event.password
                )
            }
            is SignupEvent.EnterPasswordConfirm -> {
                _signupDTO.value = _signupDTO.value.copy(
                    password2 = event.passwordConfirm
                )
            }
            is SignupEvent.Signup -> {
                _signupDTO.value = _signupDTO.value.copy(
                    role = event.role
                )
                signup()
            }
            is SignupEvent.CheckEmail -> emailCheck()
            is SignupEvent.SendEmail -> {
                _emailCodeDTO.value = _emailCodeDTO.value.copy(
                    email = emailDTO.value.email,
                    code = ""
                )
                resetTimer()
                emailSend()
            }
        }
    }

    private fun signup() {
        viewModelScope.launch {
            if (isSignupInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("입력 내용은 비어있으면 안됩니다."))
                return@launch
            }
            signupUseCase(signupDTO.value).collect { response ->
                when(response){
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> _eventFlow.emit(WasinEvent.Navigate)
                }
            }
        }
    }

    private fun emailSend() {
        viewModelScope.launch {
            if (isEmailInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("이메일은 비어있으면 안됩니다."))
                return@launch
            }
            isTimerMove.value = true
            startTimer()
            emailSendUseCase(emailDTO.value).collect { response ->
                when(response){
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> {
                        _eventFlow.emit(WasinEvent.MakeToast(response.message))
                        resetTimer()
                    }
                    is Resource.Success -> {}
                }
            }
        }
    }

    private fun emailCheck() {
        viewModelScope.launch {
            if (isEmailCodeInvalid()) {
                _eventFlow.emit(WasinEvent.MakeToast("이메일 코드는 비어있으면 안됩니다."))
                return@launch
            }
            emailCheckUseCase(emailCodeDTO.value).collect { response ->
                when(response){
                    is Resource.Loading -> _eventFlow.emit(WasinEvent.Loading)
                    is Resource.Error -> _eventFlow.emit(WasinEvent.MakeToast(response.message))
                    is Resource.Success -> {
                        resetTimer()
                        enabled.value = false
                    }
                }
            }
        }
    }

    private fun startTimer() {
        coroutineScope.launch {
            while (isTimerMove.value && timer.longValue > 0) {
                delay(1000)
                timer.longValue -= 1000L
            }

            if (timer.longValue == 0L) {
                _eventFlow.emit(WasinEvent.MakeToast("인증 번호가 만료되었습니다."))
                resetTimer()
                initEmailCodeDTO()
            }
        }
    }

    private fun initEmailCodeDTO() {
        _emailCodeDTO.value = _emailCodeDTO.value.copy(
            email = "",
            code = ""
        )
    }

    private fun resetTimer() {
        isTimerMove.value = false
        timer.longValue = TIMER_LENGTH
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
    }

    private fun emailChangedError() {
        resetTimer()
        initEmailCodeDTO()

        viewModelScope.launch {
            _eventFlow.emit(WasinEvent.MakeToast("이메일이 변경되면 안됩니다. 다시 인증번호 발송을 눌러주세요."))
        }
    }
    private fun isEmailCodeInvalid() = emailCodeDTO.value.code.isEmpty() || emailCodeDTO.value.email.isEmpty()
    private fun isEmailInvalid() = emailDTO.value.email.isEmpty()
    private fun isSignupInvalid() = signupDTO.value.role.isEmpty() || signupDTO.value.username.isEmpty() ||
        signupDTO.value.email.isEmpty() || signupDTO.value.password.isEmpty() || signupDTO.value.password2.isEmpty()

    companion object {
        private const val TIMER_LENGTH = 3000 * 60L
    }
}
