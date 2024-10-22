package com.wasin.presentation._common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wasin.presentation._theme.gray_C9C9C9
import com.wasin.presentation._theme.typography


@Composable
fun FilterDropDownButton(
    modifier: Modifier = Modifier,
    text: String = "추천순",
    selectList: List<String> = emptyList(),
    onClick: (Int) -> Unit = {},
    color: Color = gray_C9C9C9,
) {
    val isDialogOpen = remember { mutableStateOf(false) }
    if (isDialogOpen.value) {
        FilterDropDownDialog(
            selectList = selectList,
            onDismissRequest = { isDialogOpen.value = false },
            onClick = onClick
        )
    }
    Row(
        modifier = modifier
            .clickAsSingle { isDialogOpen.value = true }
            .wrapContentHeight()
            .border(BorderStroke(1.dp, color), RoundedCornerShape(30.dp))
            .padding(vertical = 5.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            tint = color,
            contentDescription = "toggle filtering list",
            modifier = Modifier.width(30.dp)
        )
        Text(
            text = text,
            style = typography.bodyMedium,
            color = color,
        )
    }
}

@Composable
fun FilterDropDownDialog(
    selectList: List<String>,
    onClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest) {
        LazyColumn (
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(30.dp)
        ) {
            itemsIndexed(selectList) { i, item ->
                Text(
                    text = item,
                    style = typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickAsSingle{
                            onClick(i)
                            onDismissRequest()
                        }
                        .padding(vertical = 15.dp)
                )
                GrayDivider()
            }
        }
    }

}
