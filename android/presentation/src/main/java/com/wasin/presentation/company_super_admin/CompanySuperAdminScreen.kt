package com.wasin.presentation.company_super_admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.wasin.data.model.company.FindAllCompanyByOpenAPIResponse
import com.wasin.presentation.R
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.GrayDivider
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.TextField
import com.wasin.presentation._common.TextFieldCardWithTitle
import com.wasin.presentation._common.TextFieldWithTitle
import com.wasin.presentation._common.WithTitle
import com.wasin.presentation._common.clickAsSingle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.gray_C9C9C9
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.FileUtil
import com.wasin.presentation._util.LaunchedEffectEvent
import com.wasin.presentation._util.WasinBackHandler
import java.io.File

@Composable
fun CompanySuperAdminScreen(
    navController: NavController,
    viewModel: CompanySuperAdminViewModel = hiltViewModel()
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    if (isDialogOpen.value) {
        CompanyDialog (
            companyList = viewModel.companyList.value.companyOpenAPIList,
            companyName = viewModel.companyDTO.value.companyName,
            enterCompany = { viewModel.onEvent(CompanySuperAdminEvent.EnterCompany(it)) },
            searchCompany = { viewModel.onEvent(CompanySuperAdminEvent.SearchCompany) },
            selectCompany = { a, b, c -> viewModel.onEvent(CompanySuperAdminEvent.SelectCompany(a, b, c)) },
            onDismissRequest = { isDialogOpen.value = false }
        )
    }
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
        title = "회사 정보 입력"
    ) {
        item {
            TextFieldWithTitle(
                title = "보안코드",
                placeholder = "와신상담의 보안 코드를 입력해주세요.",
                text = viewModel.companyDTO.value.serviceKey,
                onValueChange = { viewModel.onEvent(CompanySuperAdminEvent.EnterCode(it)) }
            )
        }
        item {
            TextFieldCardWithTitle(
                title = "회사",
                placeholder = "회사 이름을 입력해주세요.",
                text = viewModel.companyDTO.value.companyName,
                onClick = { isDialogOpen.value = true }
            )
        }
        item {
            CompanyImageInput() {
                viewModel.onEvent(CompanySuperAdminEvent.EnterImage(it))
            }
        }
        item {
            BlueLongButton(text = "등록 완료") {
                viewModel.onEvent(CompanySuperAdminEvent.Save)
            }
        }
    }
}

@Composable
fun CompanyDialog(
    companyName: String = "",
    enterCompany: (String) -> Unit,
    searchCompany: () -> Unit,
    selectCompany: (String, String, String) -> Unit,
    onDismissRequest: () -> Unit = {},
    companyList: List<FindAllCompanyByOpenAPIResponse.CompanyOpenAPIItem>
) {
    Dialog(onDismissRequest) {
        Column (
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(30.dp)
        ) {
            Text(
                text = "회사 찾기",
                style = typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Box {
                TextField(
                    text = companyName,
                    placeholder = "회사 이름을 입력해주세요.",
                    onValueChange = enterCompany,
                    onDone = searchCompany,
                    modifier = Modifier.padding(top = 30.dp, bottom = 25.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "search company",
                    modifier = Modifier
                        .padding(top = 5.dp, end = 20.dp)
                        .clickAsSingle(onClick = searchCompany)
                        .height(20.dp)
                        .align(Alignment.CenterEnd)
                )
            }
            if (companyList.isEmpty()) {
                MyEmptyContent()
            }
            else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(companyList) {
                        CompanyItemComponent(
                            companyName = it.companyName,
                            location = it.location,
                            onClick = {
                                selectCompany(it.companyName, it.companyFssId, it.location)
                                onDismissRequest()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyItemComponent(
    companyName: String,
    location: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickAsSingle(onClick = onClick)
    ) {
        Text(
            text = companyName,
            style = typography.bodyLarge
        )
        Text(
            text = location,
            style = typography.bodySmall,
            color = gray_808080,
            modifier = Modifier.padding(top = 7.dp, bottom = 10.dp)
        )
        GrayDivider()
    }
}


@Composable
fun CompanyImageInput(
    onImage: (File) -> Unit = {}
) {
    val context = LocalContext.current
    var image by remember { mutableStateOf(Uri.EMPTY) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { it?.let {
        image = it
        onImage(FileUtil.fileFromContentUri(it, context))
    }}

    Column {
        Text(
            text = "건물 설계도",
            style = typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(BorderStroke(1.dp, gray_C9C9C9), RoundedCornerShape(10.dp))
                .clickAsSingle { galleryLauncher.launch("image/*") }
                .padding(vertical = if (image.equals(Uri.EMPTY)) 60.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (image.equals(Uri.EMPTY)) SelectImage()
            else PreviewImage(image)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PreviewImage(
    image: Uri
) {
    AsyncImage(
        model = image,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    )
}

@Composable
fun SelectImage() {
    Image(
        painter = painterResource(id = R.drawable.camera),
        contentDescription = "add building image",
        modifier = Modifier.width(60.dp)
    )
    Text(
        text = "이미지 추가",
        style = typography.bodyLarge,
        modifier = Modifier.padding(top = 10.dp)
    )
}
