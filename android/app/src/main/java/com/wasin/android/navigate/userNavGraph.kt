package com.wasin.android.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation.login.LoginScreen
import com.wasin.presentation.signup.SignupScreen
import com.wasin.presentation.wifi_list.WifiScreen

fun NavGraphBuilder.userNavGraph(
    navController: NavController
) {
    composable(
        route = WasinScreen.WifiListScreen.route,
        content = { WifiScreen(navController) }
    )
    composable(
        route = WasinScreen.LoginScreen.route,
        content = {
            LoginScreen(
                navController = navController,
                nextScreen = WasinScreen.WifiListScreen.route
            )
        }
    )
    composable(
        route = WasinScreen.SignupScreen.route,
        content = { SignupScreen(navController, "user") },
    )
}
