package com.wasin.presentation._theme

import androidx.compose.ui.graphics.Color

val main_blue = Color(0xFF3A7DFF)
val main_pink = Color(0xFFFF93D4)
val main_green = Color(0xFF05EA00)
val main_red = Color(0xFFEE0000)
val main_orange = Color(0xFFFF7F00)

val light_green = Color(0xFFF1FFEB)
val light_red = Color(0xFFFEF2F2)
val light_blue = Color(0xFFECF2FF)

val gray_C9C9C9 = Color(0xFFC9C9C9)
val gray_B8B8B8 = Color(0xFFB8B8B8)
val gray_808080 = Color(0xFF808080)
val gray_E8E8E8 = Color(0xFFE8E8E8)
val gray_979797 = Color(0xFF979797)
val gray_A1A1A1 = Color(0xFFA1A1A1)

val scoreColor = listOf(
    Color.Red,
    main_orange,
    Color.Yellow,
    Color.Green,
    Color(0xFF15904C)
)

fun getScoreColor(score: Long): Color {
    val index = (score / (100f / (scoreColor.size - 1))).toInt()
    val nextIndex = (index + 1).coerceAtMost(scoreColor.size - 1)

    // 인접한 두 색상 간의 비율 계산
    val ratio = (score % (100 / (scoreColor.size - 1))) / (100f / (scoreColor.size - 1))

    // 두 색상 사이를 보간하여 색상 반환
    return lerp(scoreColor[index], scoreColor[nextIndex], ratio)
}

fun lerp(start: Color, end: Color, fraction: Float): Color {
    return Color(
        red = (start.red + (end.red - start.red) * fraction),
        green = (start.green + (end.green - start.green) * fraction),
        blue = (start.blue + (end.blue - start.blue) * fraction),
        alpha = (start.alpha + (end.alpha - start.alpha) * fraction)
    )
}
