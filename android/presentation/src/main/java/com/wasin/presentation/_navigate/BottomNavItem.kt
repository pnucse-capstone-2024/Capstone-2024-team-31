package com.wasin.presentation._navigate

import androidx.annotation.DrawableRes
import com.wasin.presentation.R

sealed class BottomNavItem(
    val title: String,
    val screenList: List<WasinScreen>,
    @DrawableRes val icon: Int
) {
    data object BackOffice: BottomNavItem(
        title = "백오피스",
        icon = R.drawable.person,
        screenList = listOf(WasinScreen.BackOfficeScreen)
    )

    data object Monitoring: BottomNavItem(
        title = "모니터링",
        icon = R.drawable.monitoring,
        screenList = listOf(WasinScreen.MonitoringScreen)
    )

    data object Router: BottomNavItem(
        title = "라우터",
        icon = R.drawable.router,
        screenList = listOf(
            WasinScreen.RouterListScreen,
            WasinScreen.RouterDetailScreen,
            WasinScreen.RouterAddScreen,
            WasinScreen.RouterUpdateScreen,
            WasinScreen.RouterCheckScreen,
            WasinScreen.RouterLogScreen,
            WasinScreen.MonitoringByRouterScreen,
        )
    )

    data object Profile: BottomNavItem(
        title = "프로파일",
        icon = R.drawable.profile,
        screenList = listOf(WasinScreen.ProfileScreen)
    )

    data object Setting: BottomNavItem(
        title = "설정",
        icon = R.drawable.setting,
        screenList = listOf(WasinScreen.SettingScreen)
    )

}
