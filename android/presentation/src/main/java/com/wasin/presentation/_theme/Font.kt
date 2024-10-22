package com.wasin.presentation._theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wasin.presentation.R

val SuiteFamily = FontFamily(
    Font(R.font.bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.heavy, FontWeight.Black, FontStyle.Normal),
    Font(R.font.regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.thin, FontWeight.Thin, FontStyle.Normal),
)

val typography = Typography(
    // 제목
    titleLarge = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
    ),
    // 소제목
    titleMedium = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
    ),
    // 버튼
    displayLarge = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    // 본문, 텍스트 필드
    bodyLarge = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // 소제목 아래 설명
    bodyMedium = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    // 본문보다 작은 글씨
    bodySmall = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SuiteFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
)
