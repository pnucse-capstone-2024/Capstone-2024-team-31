package com.wasin.presentation.lock_confirm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data._const.DataStoreKey
import com.wasin.data.datastore.WasinDataStore
import com.wasin.presentation._common.LockComponent
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.WasinBackHandler

@Composable
fun LockConfirmScreen(
    navController: NavController,
    nextScreen: String,
    viewModel: LockConfirmViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    WasinBackHandler()
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = {
            val key = DataStoreKey.ROUTER_ID_KEY.name
            val routerId = WasinDataStore(context).getData(key)
            val screen = if (routerId.isEmpty()) nextScreen else WasinScreen.RouterDetailScreen.route + "?routerId=$routerId"
            WasinDataStore(context).clear(key)

            viewModel.saveScreenState(WasinScreen.LockConfirmScreen.route)
            navController.navigate(screen){
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    )
    Column(
        modifier = Modifier.padding(top = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "잠금해제",
            style = typography.titleLarge,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        Text(
            text = "비밀번호를 입력해주세요.",
            style = typography.bodyMedium
        )
        LockComponent(
            password = viewModel.lockDTO.value,
            onAdd = { viewModel.addPassword(it) },
            onDelete = { viewModel.removeLast() },
        )
    }
}
