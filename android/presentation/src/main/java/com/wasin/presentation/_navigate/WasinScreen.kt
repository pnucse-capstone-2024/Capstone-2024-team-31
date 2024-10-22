package com.wasin.presentation._navigate

sealed class WasinScreen (val route: String){
    data object SplashScreen: WasinScreen("splash_screen")
    data object SignupScreen: WasinScreen("signup_screen")
    data object LoginScreen: WasinScreen("login_screen")
    data object BackOfficeScreen: WasinScreen("back_office_screen")
    data object SettingScreen: WasinScreen("setting_screen")
    data object CompanyAdminScreen: WasinScreen("company_admin_screen")
    data object CompanySuperAdminScreen: WasinScreen("company_super_admin_screen")
    data object LockConfirmScreen: WasinScreen("lock_confirm_screen")
    data object LockSettingScreen: WasinScreen("lock_setting_screen")
    data object MonitoringScreen: WasinScreen("monitoring_screen")
    data object MonitoringByRouterScreen: WasinScreen("monitoring_by_router_screen")
    data object ProfileScreen: WasinScreen("profile_screen")
    data object RouterAddScreen: WasinScreen("router_add_screen")
    data object RouterDetailScreen: WasinScreen("router_detail_screen")
    data object RouterListScreen: WasinScreen("router_list_screen")
    data object RouterUpdateScreen: WasinScreen("router_update_screen")
    data object RouterCheckScreen: WasinScreen("router_check_screen")
    data object RouterLogScreen: WasinScreen("router_log_screen")
    data object WifiListScreen: WasinScreen("wifi_list_screen")
}
