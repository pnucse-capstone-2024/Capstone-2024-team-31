package com.wasin.presentation._common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wasin.presentation.R
import com.wasin.presentation._theme.gray_979797

@Composable
fun MyEmptyContent(
    modifier: Modifier = Modifier,
    message: String = ""
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
    ){
        Icon(
            painter = painterResource(R.drawable.empty),
            contentDescription = "빈 화면",
            modifier = Modifier.width(130.dp),
            tint = gray_979797
        )
        Text(
            text = message.ifEmpty { "아직 내용이 비어있습니다.\n앱 내에서 추가해주세요." },
            style = MaterialTheme.typography.bodyLarge,
            color = gray_979797,
            textAlign = TextAlign.Center
        )
    }
}
