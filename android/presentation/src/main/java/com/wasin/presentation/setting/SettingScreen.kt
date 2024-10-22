package com.wasin.presentation.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation.R
import com.wasin.presentation._common.GrayDivider
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._common.clickAsSingle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.gray_A1A1A1
import com.wasin.presentation._theme.gray_C9C9C9
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = { navController.navigate(WasinScreen.LoginScreen.route) }
    )
    WithTitle (
        title = "설정"
    ) {
        item { SettingUserInfo(email = viewModel.email.value) }
        item { SettingService(version = "v1.0.0") }
        item {
            SettingMyPage(
                onLogOutClick = { viewModel.logout() },
                onWithdrawClick = { viewModel.withdraw() }
            )
        }
        item { SettingExtra() }
        item { SettingCopyRight() }
    }
}


@Composable
private fun SettingUserInfo(email: String = "") {
    SettingContentTheme("가입 정보"){
        SettingText(text = email)
    }
}

@Composable
private fun SettingService(version: String = "") {
    val urlHandler = LocalUriHandler.current
    SettingContentTheme("서비스"){
        WithArrowItem(
            text = "공지 사항",
            onClick = { urlHandler.openUri(NotionLink.NOTICE.link) }
        )
        WithArrowItem(
            text = "FAQ",
            onClick = { urlHandler.openUri(NotionLink.FAQ.link) }
        )
        SettingVersion(version)
    }
}

@Composable
private fun SettingMyPage(
    onLogOutClick: () -> Unit = {},
    onWithdrawClick: () -> Unit = {}
) {
    SettingContentTheme("마이페이지"){
        WithArrowItem(
            text = "로그아웃",
            onClick = onLogOutClick
        )
        WithArrowItem(
            text = "회원탈퇴",
            onClick = onWithdrawClick
        )
    }
}

@Composable
private fun SettingExtra() {
    val urlHandler = LocalUriHandler.current
    SettingContentTheme(
        title = "기타",
        isLast = true,
    ){
        WithArrowItem(
            text = "개인 정보 처리 방침",
            onClick = { urlHandler.openUri(NotionLink.PERSONAL.link) }
        )
        WithArrowItem(
            text = "서비스 약관",
            onClick = { urlHandler.openUri(NotionLink.SERVICE.link) }
        )
        WithArrowItem(
            text = "오픈 소스 라이브러리",
            onClick = { urlHandler.openUri(NotionLink.OPEN_SOURCE.link) }
        )
    }
}

@Composable
private fun SettingCopyRight() {
    Text(
        text = "©wasin all rights reserved",
        color = gray_808080,
        style = typography.bodySmall,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
@Composable
fun SettingContentTheme(
    title: String = "",
    isLast: Boolean = false,
    contents: @Composable (ColumnScope.() -> Unit)
){
    Column(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SettingGrayText(title)
        contents()
    }
    if (!isLast) GrayDivider()
}

@Composable
private fun SettingVersion(
    version: String = "",
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingText(text = "버전")
        SettingText(
            modifier = Modifier. align(Alignment.CenterEnd),
            text = "v$version"
        )
    }
}

@Composable
fun WithArrowItem(
    text: String = "",
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clickAsSingle(onClick = onClick)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        SettingText(
            modifier = Modifier.padding(end = 33.dp),
            text = text
        )
        SettingArrowIcon(
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun SettingArrowIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.arrow),
        contentDescription = "화살표 그림(이동하기)",
        tint = gray_C9C9C9,
        modifier = modifier.height(20.dp)
    )
}

@Composable
private fun SettingGrayText(
    text: String = ""
){
    Text(
        text = text,
        color = gray_A1A1A1,
        style = typography.bodyMedium
    )
}

@Composable
private fun SettingText(
    modifier: Modifier = Modifier,
    text: String = ""
){
    Text(
        text = text,
        style = typography.bodyLarge,
        modifier = modifier
    )
}
