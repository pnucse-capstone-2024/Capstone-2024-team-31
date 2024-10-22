package com.wasin.presentation._common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wasin.presentation.R
import com.wasin.presentation._theme.gray_808080
import com.wasin.presentation._theme.typography

@Composable
fun LockComponent(
    password: String = "",
    onAdd: (String) -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Column {
        LockInputField( password)
        LockDialKeypad(onAdd, onDelete)
    }
}

@Composable
fun LockDialKeypad(
    onAdd: (String) -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val dialList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0")
    LazyVerticalGrid(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .heightIn(max = 1200.dp)
            .wrapContentHeight(),
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        items(dialList) { DialKeypadItem(it) { onAdd(it) } }
        item { BackErase(onDelete) }
    }
}

@Composable
private fun BackErase(
    onDelete: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(50.dp)
            .clickAsSingle(onClick = onDelete),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.erase),
            contentDescription = "erase",
            modifier = Modifier.width(30.dp)
        )
    }
}

@Composable
private fun DialKeypadItem(
    num: String,
    onAdd: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(50.dp)
            .clickAsSingle(onClick = onAdd),
        contentAlignment = Alignment.Center
    ) {
        Text(text = num, style = typography.titleLarge)
    }
}

@Composable
fun LockInputField(
    password: String = ""
) {
    Row (
        modifier = Modifier
            .padding(vertical = 50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 0..3) {
            LockInputItem(password.getOrElse(i) { ' ' })
        }
    }
}

@Composable
private fun LockInputItem(num: Char) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = num.toString(),
            style = typography.titleLarge
        )
        Divider(
            thickness = 2.dp,
            color = gray_808080,
            modifier = Modifier.width(25.dp)
        )
    }
}
