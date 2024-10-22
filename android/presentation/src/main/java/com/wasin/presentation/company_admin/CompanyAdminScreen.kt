package com.wasin.presentation.company_admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.gray_979797
import com.wasin.presentation._theme.main_blue
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.WasinBackHandler

@Composable
fun CompanyAdminScreen(
    navController: NavController,
    viewModel: CompanyAdminViewModel = hiltViewModel(),
) {
    WasinBackHandler()
    LaunchedEffectEvent(
        eventFlow = viewModel.eventFlow,
        onNavigate = {
            viewModel.saveScreenState(WasinScreen.LockSettingScreen.route)
            navController.navigate(WasinScreen.LockSettingScreen.route){
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
            }
        }
    )
    WithTitle(
        title = "회사 등록"
    ) {
        val companyDBList = viewModel.companyList.value.companyDBList
        if (companyDBList.isEmpty()) {
            item { MyEmptyContent() }
        }
        else {
            items(companyDBList) { item ->
                CompanyAdminItemComponent(
                    name = item.companyName,
                    location = item.companyName,
                    isSelected = viewModel.isSelected(item.companyId),
                    onClick = { viewModel.setCompanyId(item.companyId) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
                BlueLongButton(
                    text = "등록 완료",
                    onClick = { viewModel.saveCompany() }
                )
            }
        }
    }
}

@Composable
fun CompanyAdminItemComponent(
    name: String,
    location: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
) {
    Row {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = main_blue,
                unselectedColor = gray_979797
            )
        )
        Column {
            Text(
                text = name,
                style = typography.bodyLarge
            )
            Text(
                text = location,
                style = typography.bodySmall,
                color = gray_808080,
                modifier = Modifier.padding(top = 7.dp, bottom = 10.dp)
            )
        }
    }


}
