package com.wasin.admin.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation.company_admin.CompanyAdminScreen
import com.wasin.presentation.lock_confirm.LockConfirmScreen
import com.wasin.presentation.login.LoginScreen
import com.wasin.presentation.signup.SignupScreen

fun NavGraphBuilder.adminNavGraph(
    navController: NavController
) {
    composable(
        route = WasinScreen.CompanyAdminScreen.route,
        content = { CompanyAdminScreen(navController) }
    )
    composable(
        route = WasinScreen.LockConfirmScreen.route,
        content = {
            LockConfirmScreen(
                navController = navController,
                nextScreen = WasinScreen.MonitoringScreen.route,
            )
        }
    )
    composable(
        route = WasinScreen.LoginScreen.route,
        content = {
            LoginScreen(
                navController = navController,
                nextScreen = WasinScreen.CompanyAdminScreen.route
            )
        }
    )
    composable(
        route = WasinScreen.SignupScreen.route,
        content = { SignupScreen(navController, "admin") },
    )
}
