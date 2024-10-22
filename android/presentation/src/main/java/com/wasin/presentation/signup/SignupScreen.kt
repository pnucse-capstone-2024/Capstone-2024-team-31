package com.wasin.presentation.signup

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.DefaultButton
import com.wasin.presentation._common.GrayLongButton
import com.wasin.presentation._common.TextField
import com.wasin.presentation._common.TextFieldWithTitle
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.main_blue
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.timerFormat

@Composable
fun SignupScreen(
    navController: NavController,
    role: String,
    viewModel: SignupViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = { navController.navigate(WasinScreen.LoginScreen.route) },
    )

    WithTitle(
        title = "회원가입"
    ) {
        item {
            NameField(viewModel.signupDTO.value.username) {
                viewModel.onEvent(SignupEvent.EnterName(it))
            }
        }
        item {
            EmailField(
                email = viewModel.emailDTO.value.email,
                emailCode = viewModel.emailCodeDTO.value.code,
                onEmail = { viewModel.onEvent(SignupEvent.EnterEmail(it)) },
                onEmailCode = { viewModel.onEvent(SignupEvent.EnterEmailCode(it)) },
                sendEmail = { viewModel.onEvent(SignupEvent.SendEmail) },
                checkEmail = { viewModel.onEvent(SignupEvent.CheckEmail) },
                enabled = viewModel.enabled.value,
                timer = viewModel.timer.longValue,
                isTimerMove = viewModel.isTimerMove.value,
            )
        }
        item {
            PasswordField(viewModel.signupDTO.value.password) {
                viewModel.onEvent(SignupEvent.EnterPassword(it))
            }
        }
        item {
            PasswordConfirmField(viewModel.signupDTO.value.password2) {
                viewModel.onEvent(SignupEvent.EnterPasswordConfirm(it))
            }
        }
        item { Spacer(modifier = Modifier.height(5.dp)) }
        item {
            BlueLongButton(text = "가입 완료") {
                viewModel.onEvent(SignupEvent.Signup(role))
            }
        }
    }
}


@Composable
fun NameField(
    username: String,
    onValueChange: (String) -> Unit = {}
) {
    TextFieldWithTitle(
        title = "이름",
        text = username,
        onValueChange = onValueChange,
        placeholder = "3~10자 이내로 입력해주세요.",
    )
}

@Composable
fun EmailField(
    email: String,
    emailCode: String,
    onEmail: (String) -> Unit = {},
    onEmailCode: (String) -> Unit = {},
    sendEmail: () -> Unit = {},
    checkEmail: () -> Unit = {},
    enabled: Boolean,
    timer: Long,
    isTimerMove: Boolean
) {
    Column {
        TextFieldWithTitle(
            title = "이메일",
            text = email,
            onValueChange = onEmail,
            placeholder = "이메일 형식을 지켜주세요.",
            keyboardType = KeyboardType.Email,
            enabled = enabled
        )
        EmailCodeField(emailCode, onEmailCode,
            sendEmail, enabled, timer, isTimerMove)
        GrayLongButton(
            text = "인증번호 확인",
            onClick = checkEmail,
            enabled = enabled
        )
    }
}

@Composable
fun EmailCodeField(
    emailCode: String,
    onEmailCode: (String) -> Unit,
    sendEmail: () -> Unit = {},
    enabled: Boolean,
    timer: Long,
    isTimerMove: Boolean
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(top = 9.dp, bottom = 18.dp)
            .fillMaxWidth()
            .height(44.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.weight(1.4f)
        ) {
            TextField(
                text = emailCode,
                onValueChange = {
                    if (isTimerMove) onEmailCode(it)
                    else Toast.makeText(context, "인증 번호 발송을 먼저 진행해 주세요.", Toast.LENGTH_SHORT).show()
                },
                placeholder = "인증번호",
                keyboardType = KeyboardType.Number,
                enabled = enabled
            )
            Text(
                text = timer.timerFormat(),
                style = typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .align(Alignment.CenterEnd),
                color = Color.Red
            )
        }
        DefaultButton(
            text = "인증번호 발송",
            modifier = Modifier.weight(1f),
            color = Color.White,
            textColor = main_blue,
            border = BorderStroke(1.dp, main_blue),
            style = typography.bodyLarge,
            enabled = enabled,
            onClick = sendEmail
        )
    }
}

@Composable
fun PasswordField(
    password: String,
    onValueChange: (String) -> Unit = {}
) {
    TextFieldWithTitle(
        title = "패스워드",
        text = password,
        onValueChange = onValueChange,
        placeholder = "숫자, 영어, 특수문자를 포함하여 작성해주세요.",
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun PasswordConfirmField(
    password: String,
    onValueChange: (String) -> Unit = {}
) {
    TextFieldWithTitle(
        title = "패스워드 확인",
        text = password,
        onValueChange = onValueChange,
        placeholder = "한 번 더 패스워드를 입력해주세요.",
        keyboardType = KeyboardType.Password,
        visualTransformation = PasswordVisualTransformation()
    )
}
