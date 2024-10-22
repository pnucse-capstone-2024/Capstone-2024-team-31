package com.wasin.super_admin.navigate

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation.backoffice.BackOfficeScreen
import com.wasin.presentation.company_super_admin.CompanySuperAdminScreen
import com.wasin.presentation.lock_confirm.LockConfirmScreen
import com.wasin.presentation.login.LoginScreen
import com.wasin.presentation.signup.SignupScreen

fun NavGraphBuilder.superAdminNavGraph(
    navController: NavController
) {
    composable(
        route = WasinScreen.BackOfficeScreen.route,
        content = { BackOfficeScreen(navController) }
    )
    composable(
        route = WasinScreen.CompanySuperAdminScreen.route,
        content = { CompanySuperAdminScreen(navController) }
    )
    composable(
        route = WasinScreen.LockConfirmScreen.route,
        content = {
            LockConfirmScreen(
                navController = navController,
                nextScreen = WasinScreen.BackOfficeScreen.route,
            )
        }
    )
    composable(
        route = WasinScreen.LoginScreen.route,
        content = {
            LoginScreen(
                navController = navController,
                nextScreen = WasinScreen.CompanySuperAdminScreen.route
            )
        }
    )
    composable(
        route = WasinScreen.SignupScreen.route,
        content = { SignupScreen(navController, "super_admin") },
    )
}
