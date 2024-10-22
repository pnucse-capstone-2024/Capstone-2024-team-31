package com.wasin.presentation._common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wasin.presentation._theme.gray_C9C9C9
import com.wasin.presentation._theme.gray_E8E8E8
import com.wasin.presentation._theme.main_blue
import com.wasin.presentation._theme.typography
import com.wasin.presentation._util.NoRippleInteractionSource

@Composable
fun BlueLongButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    DefaultButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun WhiteLongButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {}
) {
    DefaultButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        textColor = main_blue,
        border = BorderStroke(1.dp, main_blue),
    )
}

@Composable
fun GrayLongButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    DefaultButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = gray_E8E8E8,
        textColor = Color.Black,
        style = typography.bodyLarge,
        verticalPadding = 9.dp,
        enabled = enabled,
    )
}

@Composable
fun ShortButton(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    DefaultButton(
        text = text,
        modifier = modifier.wrapContentWidth(),
        color = if(isSelected) main_blue else Color.White,
        textColor = if(isSelected) Color.White else Color.Black,
        border = if(isSelected) null else BorderStroke(1.dp, gray_C9C9C9),
        shape = RoundedCornerShape(30.dp),
        style = typography.titleSmall,
        horizontalPadding = 20.dp,
        verticalPadding = 5.dp,
        onClick = onClick,
    )
}

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "",
    color: Color = main_blue,
    textColor: Color = Color.White,
    border: BorderStroke? = null,
    shape: Shape = RoundedCornerShape(10.dp),
    horizontalPadding: Dp = 15.dp,
    verticalPadding: Dp = 13.dp,
    style: TextStyle = typography.titleMedium,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier.wrapContentHeight(),
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        interactionSource = NoRippleInteractionSource,
        border = if (enabled) border else null,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor,
        ),
    ){
        Text(
            text = text,
            style = style,
            textAlign = TextAlign.Center
        )
    }
}
