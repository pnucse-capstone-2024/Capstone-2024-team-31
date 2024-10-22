package com.wasin.presentation.login

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.data.model.user.LoginRequest
import com.wasin.presentation.R
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.MyDialog
import com.wasin.presentation._common.TextField
import com.wasin.presentation._common.WhiteLongButton
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._util.LaunchedEffectEvent

@Composable
fun LoginScreen(
    navController: NavController,
    nextScreen: String,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = {
            viewModel.saveScreenState(nextScreen)
            navController.navigate(nextScreen) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    )
    RequestNotificationPermissionDialog(
        nextScreen == WasinScreen.WifiListScreen.route
    )
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { WasinLogo() }
        item {
            LoginTextField(
                loginDTO = viewModel.loginDTO.value,
                onEmail = { viewModel.onEvent(LoginEvent.EnterEmail(it)) },
                onPassword = { viewModel.onEvent(LoginEvent.EnterPassword(it)) }
            )
        }
        item { LoginButton { viewModel.onEvent(LoginEvent.Login) } }
        item { SignupButton{ navController.navigate(WasinScreen.SignupScreen.route) } }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog(
    withLocationPermission: Boolean
) {
    val context = LocalContext.current
    val key = DataStoreKey.NOTIFICATION_PERMISSION_KEY.name
    val isOpen = remember { mutableStateOf(WasinDataStore(context).getData(key).isEmpty()) }
    val permissionState =  rememberMultiplePermissionsState(
        if (withLocationPermission) {
            listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else {
            listOf(Manifest.permission.POST_NOTIFICATIONS)
        }
    )

    MyDialog(
        text = "'와신상담'에선 주요 기능을 사용하기 위해선 푸시 알림과 위치 권한이 필요합니다.\n\n" +
                "해당 권한에 수신 동의하시겠습니까?\n\n" +
                "권한은 [설정 > 알림 설정] 에서 변경하실 수 있습니다. ",
        isOpen = isOpen,
        onCancelClick = {
            makePermissionToastMessage(context)
        },
        onConfirmClick = {
            permissionState.launchMultiplePermissionRequest()
            WasinDataStore(context).setData(key, "true")
        }
    )
}

private fun makePermissionToastMessage(context: Context) {
    Toast.makeText(context, "알림설정을 허용하지 않으면 와이파이 조회 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
}


@Composable
fun WasinLogo() {
    Image(
        painter = painterResource(id = R.drawable.wasin_logo),
        contentDescription = "wasin_logo",
        modifier = Modifier.height(200.dp)
    )
}

@Composable
fun LoginTextField(
    loginDTO: LoginRequest,
    onEmail: (String) -> Unit = {},
    onPassword: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.padding(top = 65.dp, bottom = 53.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
            text = loginDTO.email,
            placeholder = "아이디(이메일)",
            onValueChange = onEmail,
            keyboardType = KeyboardType.Email
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            text = loginDTO.password,
            placeholder = "비밀번호",
            onValueChange = onPassword,
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

@Composable
fun LoginButton(
    onClick: () -> Unit
) {
    BlueLongButton(
        text = "로그인",
        onClick = onClick,
        modifier = Modifier.padding(bottom = 14.dp)
    )
}

@Composable
fun SignupButton(
    onClick: () -> Unit
) {
    WhiteLongButton(
        text = "회원가입",
        onClick = onClick
    )
}
