package com.wasin.presentation._navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wasin.presentation.lock_setting.LockSettingScreen
import com.wasin.presentation.monitoring.MonitoringScreen
import com.wasin.presentation.monitoring_by_router.MonitoringByRouterScreen
import com.wasin.presentation.profile.ProfileScreen
import com.wasin.presentation.router_add.RouterAddScreen
import com.wasin.presentation.router_check.RouterCheckScreen
import com.wasin.presentation.router_detail.RouterDetailScreen
import com.wasin.presentation.router_list.RouterListScreen
import com.wasin.presentation.router_log.RouterLogScreen
import com.wasin.presentation.router_update.RouterUpdateScreen
import com.wasin.presentation.setting.SettingScreen
import com.wasin.presentation.splash.SplashScreen

fun NavGraphBuilder.commonNavGraph(
    navController: NavController
) {
    composable(
        route = WasinScreen.SplashScreen.route,
        content = { SplashScreen(navController) }
    )
    composable(
        route = WasinScreen.SettingScreen.route,
        content = { SettingScreen(navController) }
    )
}

fun NavGraphBuilder.commonAdminNavGraph(
    navController: NavController
) {
    composable(
        route = WasinScreen.MonitoringScreen.route,
        content = { MonitoringScreen(navController) }
    )
    composable(
        route = WasinScreen.ProfileScreen.route,
        content = { ProfileScreen(navController) }
    )
    composable(
        route = WasinScreen.RouterAddScreen.route,
        content = { RouterAddScreen(navController) }
    )
    composable(
        route = WasinScreen.RouterListScreen.route,
        content = { RouterListScreen(navController) }
    )
    composable(
        route = WasinScreen.LockSettingScreen.route,
        content = { LockSettingScreen(navController) }
    )
    composable(
        route = WasinScreen.MonitoringByRouterScreen.route + "?routerId={routerId}",
        arguments = listOf(
            navArgument("routerId"){
                type = NavType.LongType
                defaultValue = -1L
            },
        ),
        content = { MonitoringByRouterScreen(navController) }
    )
    composable(
        route = WasinScreen.RouterDetailScreen.route + "?routerId={routerId}",
        arguments = listOf(
            navArgument("routerId"){
                type = NavType.LongType
                defaultValue = -1L
            },
        ),
        content = {
            RouterDetailScreen(
                routerId = it.arguments?.getLong("routerId") ?: -1,
                navController = navController
            )
        }
    )
    composable(
        route = WasinScreen.RouterUpdateScreen.route + "?routerId={routerId}",
        arguments = listOf(
            navArgument("routerId"){
                type = NavType.LongType
                defaultValue = -1L
            },
        ),
        content = { RouterUpdateScreen(navController) }
    )
    composable(
        route = WasinScreen.RouterCheckScreen.route + "?routerId={routerId}",
        arguments = listOf(
            navArgument("routerId"){
                type = NavType.LongType
                defaultValue = -1L
            },
        ),
        content = { RouterCheckScreen(navController) }
    )
    composable(
        route = WasinScreen.RouterLogScreen.route + "?routerId={routerId}",
        arguments = listOf(
            navArgument("routerId"){
                type = NavType.LongType
                defaultValue = -1L
            },
        ),
        content = { RouterLogScreen(navController) }
    )
}
