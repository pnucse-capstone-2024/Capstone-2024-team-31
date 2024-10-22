package com.wasin.presentation._common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.wasin.presentation.R
import com.wasin.presentation._theme.typography


@Composable
fun WithTitle(
    title: String = "",
    description: String? = null,
    containSetting: Boolean = false,
    onSettingClick: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { TitleAndContents(title, description, containSetting, onSettingClick) }
        content()
        item { Spacer(modifier = Modifier.height(10.dp)) }
    }
}

@Composable
fun WithTitleAndRefresh(
    onRefresh: () -> Unit,
    title: String = "",
    description: String? = null,
    containSetting: Boolean = false,
    onSettingClick: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh,
        indicator = { state, trigger -> MySwipeRefreshIndicator(state, trigger) }
    ) {
        WithTitle(title, description, containSetting, onSettingClick, content)
    }
}

@Composable
fun TitleAndContents(
    text: String,
    description: String?,
    containSetting: Boolean,
    onSettingClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Text(
                text = text,
                style = typography.titleLarge
            )
            GrayDivider(
                modifier = Modifier.padding(top = 8.dp, bottom = 15.dp)
            )
            if (description != null) {
                Text(
                    text = description,
                    style = typography.bodyMedium
                )
            }
        }
        if (containSetting) {
            SettingImage(
                modifier = Modifier.clickAsSingle(onClick = onSettingClick)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun SettingImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.setting),
        contentDescription = "setting",
        modifier = modifier
            .padding(top = 33.dp)
            .height(22.dp)
    )
}
