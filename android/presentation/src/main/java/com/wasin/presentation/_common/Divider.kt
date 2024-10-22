package com.wasin.presentation._common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wasin.presentation._theme.gray_E8E8E8

@Composable
fun GrayDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = gray_E8E8E8
    )
}
