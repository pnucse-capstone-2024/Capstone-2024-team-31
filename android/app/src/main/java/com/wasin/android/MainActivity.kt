package com.wasin.android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.wasin.android.navigate.userNavGraph
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._navigate.commonNavGraph
import com.wasin.presentation._theme.WasinAppTheme
import com.wasin.presentation._util.WasinWorkManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        scheduleNotification()
        setContent {
            val navController = rememberNavController()
            WasinAppTheme(
                bottomBar = {},
                content = {
                    NavHost(
                        navController = navController,
                        startDestination = WasinScreen.SplashScreen.route,
                        route = "wasin_user_route"
                    ){
                        commonNavGraph(navController = navController)
                        userNavGraph(navController = navController)
                    }
                }
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("wasin channel", "와신 상담", importance).apply {
                description = "새로운 최적 와이파이를 발견했습니다."
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification() {
        val saveRequest = PeriodicWorkRequestBuilder<WasinWorkManager>(15, TimeUnit.MINUTES)
            .setInitialDelay(1000*60, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(saveRequest)
    }
}
