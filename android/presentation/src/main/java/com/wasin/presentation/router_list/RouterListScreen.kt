package com.wasin.presentation.router_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data.model.router.FindAllRouterResponse
import com.wasin.presentation._common.BlueLongButton
import com.wasin.presentation._common.CompanyImageItem
import com.wasin.presentation._common.ImageMarker
import com.wasin.presentation._common.MyEmptyContent
import com.wasin.presentation._common.WithTitleAndRefresh
import com.wasin.presentation._common.clickAsSingle
import com.wasin.presentation._navigate.WasinScreen
import com.wasin.presentation._theme.getScoreColor
import com.wasin.presentation._theme.gray_E8E8E8
import com.wasin.presentation._theme.main_orange
import com.wasin.presentation._theme.scoreColor
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent
import kotlin.math.roundToInt

@Composable
fun RouterListScreen(
    navController: NavController,
    viewModel: RouterListViewModel = hiltViewModel()
) {
    val routerList = viewModel.routerListDTO.value.routerList
    LaunchedEffectEvent(viewModel.eventFlow)
    WithTitleAndRefresh(
        onRefresh = { viewModel.refresh() },
        title = "라우터 관리",
        description = "라우터 상태에 따라 원활할 경우 초록색, 불안정할 경우 빨간색으로 나타나요. \n" +
                "마커를 클릭하면 개별 라우터에 대한 상세 정보를 확인 및 편집하고 모니터링을 진행할 수 있어요."
    ) {
        item {
            CompanyImageComponent(
                imageUrl = viewModel.routerListDTO.value.image.companyImage,
                routerList = viewModel.routerListDTO.value.routerList,
                enterImageSize = { viewModel.enterImageSize(it) },
                onClick = { navController.navigate(WasinScreen.RouterDetailScreen.route + "?routerId=${it}") }
            )
        }
        item { NetworkStateMessage() }
        item {
            BlueLongButton(
                text = "라우터 추가",
                onClick = { navController.navigate(WasinScreen.RouterAddScreen.route) }
            )
        }
        item {
            Text(
                text = "Wifi 목록",
                style = typography.titleMedium,
                modifier = Modifier.padding(top = 25.dp)
             )
        }
        if (routerList.isEmpty()) {
            item { MyEmptyContent() }
        }
        else {
            items(routerList) {
                RouterItemComponent(
                    name = it.name,
                    score = it.score,
                    onClick = { navController.navigate(WasinScreen.RouterDetailScreen.route + "?routerId=${it.routerId}") }
                )
            }
        }
    }
}

@Composable
fun NetworkStateMessage() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, gray_E8E8E8), RoundedCornerShape(30.dp))
            .padding(vertical = 20.dp)
    ) {
        Text(
            text = "라우터 상태에 따라 원활할 경우 초록색, \n" +
                    "불안정할 경우 빨간색, \n" +
                    "고장일 경우 검은색으로 나타나요. ",
            style = typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .height(20.dp)
                .fillMaxWidth()
                .background(Brush.horizontalGradient(scoreColor))
        )
        ScoreDescription()
    }
}

@Composable
private fun ScoreDescription() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp)
    ) {
        Text(
            text = "혼잡",
            style = typography.bodyLarge,
            color = Color.Red,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "보통",
            style = typography.bodyLarge,
            color = main_orange,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = "쾌적",
            style = typography.bodyLarge,
            color = Color.Green,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun CompanyImageComponent(
    imageUrl: String,
    routerList: List<FindAllRouterResponse.EachRouter> = emptyList(),
    enterImageSize: (Int) -> Unit,
    onClick: (Long) -> Unit
) {
    var localWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(true) {
        enterImageSize(localWidth)
    }
    Box (
        modifier = Modifier.fillMaxWidth()
    ) {
        CompanyImageItem(
            modifier = Modifier
                .onGloballyPositioned {
                    localWidth = it.size.width
                }
            ,
            imageUrl = imageUrl
        )
        routerList.forEach {
            ImageMarker(
                modifier = Modifier.absoluteOffset {
                    IntOffset(it.positionX.roundToInt(), it.positionY.roundToInt())
                },
                color = getScoreColor(it.score),
                onClick = { onClick(it.routerId) },
            )
        }
    }

}

@Composable
fun RouterItemComponent(
    name: String = "휴게소 Wifi ",
    score: Long = 0L,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .clickAsSingle(onClick = onClick)
            .border(BorderStroke(0.5.dp, gray_E8E8E8), RoundedCornerShape(30.dp))
            .padding(horizontal = 30.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = typography.titleMedium,
        )
        Text(
            text = "$score 점",
            style = typography.titleMedium,
            color = getScoreColor(score)
        )
    }
}
