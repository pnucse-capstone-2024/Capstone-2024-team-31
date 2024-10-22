package com.wasin.presentation._navigate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wasin.presentation._common.GrayDivider
import com.wasin.presentation._theme.gray_E8E8E8
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.NoRippleInteractionSource

@Composable
fun BottomNavigationBar(
    navController: NavController,
    screens: List<BottomNavItem>
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (shouldShowBottomNavigation(navController)) {
        Column {
            GrayDivider()
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .height(64.dp)
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                screens.forEach { screen ->
                    AddItem(
                        isSmall = screens.size == 5,
                        bottomNavItem = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
private fun shouldShowBottomNavigation(
    navController: NavController
): Boolean {
    val shouldNotShown = listOf(
        WasinScreen.SplashScreen.route,
        WasinScreen.LoginScreen.route,
        WasinScreen.SignupScreen.route,
        WasinScreen.CompanyAdminScreen.route,
        WasinScreen.CompanySuperAdminScreen.route,
        WasinScreen.LockConfirmScreen.route,
        WasinScreen.LockSettingScreen.route,
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    return currentRoute !in shouldNotShown
}

@Composable
private fun RowScope.AddItem(
    bottomNavItem: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavController,
    isSmall: Boolean
){
    NavigationBarItem (
        icon = { NavigationItem(bottomNavItem, isSmall) },
        selected = isSelected(currentDestination, bottomNavItem),
        onClick = {
            navController.navigate(bottomNavItem.screenList.first().route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Black,
            unselectedIconColor = gray_E8E8E8,
            indicatorColor = Color.White,
        ),
        interactionSource = NoRippleInteractionSource
    )
}

@Composable
private fun isSelected(
    currentDestination: NavDestination?,
    bottomNavItem: BottomNavItem
): Boolean {
    return currentDestination?.hierarchy?.any { navDestination ->
        val route = navDestination.route?.split("?")?.get(0) ?: ""
        bottomNavItem.screenList.map { screen -> screen.route }.contains(route)
    } == true
}

@Composable
private fun NavigationItem(
    bottomNavItem: BottomNavItem,
    isSmall: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(bottomNavItem.icon),
            contentDescription = bottomNavItem.title,
            modifier = Modifier.size(if(isSmall) 26.dp else 30.dp)
        )
        Text(
            text = bottomNavItem.title,
            style = typography.labelMedium
        )
    }
}
