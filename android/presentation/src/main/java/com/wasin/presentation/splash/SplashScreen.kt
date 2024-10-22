package com.wasin.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation.R
import com.wasin.presentation._util.WasinBackHandler
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    WasinBackHandler()
    LaunchedEffect(key1 = true){
        delay(500)
        navController.popBackStack()
        navController.navigate(viewModel.getScreen())
    }
    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.wasin_logo),
            contentDescription = "wasin_logo",
            modifier = Modifier
                .height(200.dp)
                .align(Alignment.Center)
        )
    }
}
