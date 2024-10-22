package com.wasin.presentation._common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wasin.presentation._theme.main_green

@Composable
fun CompanyImageItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://www.knsu.ac.kr/_res/knsu/dormitory/img/facility/img-guide-map01.JPG",
    contentDescription: String = "",
) {
    Column {
        Spacer(Modifier.height(20.dp))
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ImageMarker(
    modifier: Modifier = Modifier,
    color: Color = main_green,
    onClick: () -> Unit = {}
) {
    Icon(
        modifier = modifier.offset(-20.dp, -20.dp)
            .size(40.dp)
            .clickAsSingle(onClick = onClick),
        imageVector = Icons.Filled.LocationOn,
        contentDescription = "화살표 그림(이동하기)",
        tint = color,
    )
}
