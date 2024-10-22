package com.wasin.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wasin.data.model.profile.FindAllProfileResponse
import com.wasin.presentation._common.MyCircularProgress
import com.wasin.presentation._common.WithTitleAndRefresh
import com.wasin.presentation._common.clickAsSingle
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.gray_B8B8B8
import com.wasin.presentation._theme.gray_E8E8E8
import com.wasin.presentation._theme.light_blue
import com.wasin.presentation._theme.main_blue
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.LaunchedEffectEvent

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffectEvent(eventFlow = viewModel.eventFlow)
    WithTitleAndRefresh(
        onRefresh = { viewModel.findAllProfile() },
        title = "프로파일 설정",
        description = "프로파일에 따라 여러 공유기의 설정을 한 번에 변경할 수 있어요. 자동 변경일 경우 상황에 따라 최적의 프로파일을 시스템이 알아서 찾아서 변경해줘요."
    ) {
        item {
            ProfileModeSelect(
                isAuto = viewModel.profileDTO.value.isAuto,
                changeModeAuto = { viewModel.changeModeAuto() },
                changeModeManual = { viewModel.changeModeManual() }
            )
        }
        item {
            ProfileSelect(
                enabled = !viewModel.profileDTO.value.isAuto,
                activeProfileId = viewModel.profileDTO.value.activeProfileId,
                profileList = viewModel.profileDTO.value.profiles,
                changeProfile = { viewModel.changeProfile(it) },
                isLoading = viewModel.isLoading.value
            )
        }
    }
}

@Composable
fun ProfileModeSelect(
    isAuto: Boolean,
    changeModeAuto: () -> Unit,
    changeModeManual: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileMode(
            modifier = Modifier.weight(1f),
            text = "프로파일 자동 변경",
            isSelected = isAuto,
            onClick = changeModeAuto
        )
        Spacer(modifier = Modifier.width(10.dp))
        ProfileMode(
            Modifier.weight(1f),
            text = "프로파일 수동 변경",
            isSelected = !isAuto,
            onClick = changeModeManual
        )
    }
}

@Composable
fun ProfileMode(
    modifier: Modifier = Modifier,
    text: String = "",
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val (border, background) = selectedStyle(isSelected)
    val style = if (isSelected) typography.titleMedium else typography.displayLarge

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .clickAsSingle(onClick = onClick)
            .border(border, RoundedCornerShape(30.dp))
            .background(background)
            .padding(40.dp, 20.dp)
    ) {
        Text(
            text = text,
            style = style
        )
    }
}

@Composable
private fun selectedStyle(isSelected: Boolean): Pair<BorderStroke, Color> {
    val border = if (isSelected) BorderStroke(2.dp, main_blue) else BorderStroke(1.dp, gray_E8E8E8)
    val background = if (isSelected) light_blue else Color.White
    return Pair(border, background)
}


@Composable
fun ProfileSelect(
    enabled: Boolean,
    activeProfileId: Long,
    changeProfile: (Long) -> Unit,
    profileList: List<FindAllProfileResponse.ProfileEachDTO>,
    isLoading: Boolean
) {
    Column {
        Text(
            text = "프로파일",
            style = typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = "자동 모드일 때는 임의로 프로파일을 변경할 수 없어요.",
            style = typography.bodyLarge,
            modifier = Modifier.padding(top = 11.dp, bottom = 21.dp)
        )
        if (isLoading) {
            MyCircularProgress()
        }
        else {
            profileList.forEach {
                ProfileComponent(
                    title = it.title,
                    description = it.description,
                    tip = it.tip,
                    isSelected = it.profileId == activeProfileId,
                    onClick = { changeProfile(it.profileId) },
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
fun ProfileComponent(
    modifier: Modifier = Modifier,
    title: String = "사용자 수 제한",
    description: String = "트래픽이 하나의 와이파이에 몰리면 사용자 수를 제한해줘요.",
    tip: String = "⭐ 축제 등 사용자가 갑자기 늘어날 때 사용하기 좋아요!",
    isSelected: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    var (border, background) = selectedStyle(isSelected)
    if (!enabled) {
        border = BorderStroke(1.dp, gray_E8E8E8)
        background = gray_E8E8E8
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickAsSingle(enabled = enabled, onClick = onClick)
            .border(border, RoundedCornerShape(30.dp))
            .background(background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = typography.titleSmall,
            color = if (enabled) Color.Black else gray_B8B8B8
        )
        Text(
            text = description,
            style = typography.bodyLarge,
            color = if (enabled) gray_808080 else gray_B8B8B8,
        )
        Text(
            text = tip,
            style = typography.bodySmall,
            color = if (enabled) gray_808080 else gray_B8B8B8,
            modifier = Modifier.padding(top = 15.dp)
        )
    }
}
