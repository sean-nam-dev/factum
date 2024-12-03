package com.devflow.factum.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devflow.factum.R

private val tinkoffSansFamily = FontFamily(
    Font(R.font.tinkoffsans_bold, FontWeight.Bold),
    Font(R.font.tinkoffsans_medium, FontWeight.Medium),
    Font(R.font.tinkoffsans_regular, FontWeight.Normal),
    Font(R.font.tinkoffsans_regular, FontWeight.Light),
)

val Typography = Typography(
    headlineSmall = TextStyle(
        color = White99,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = tinkoffSansFamily,
        lineHeight = 29.sp
    ),
    titleLarge = TextStyle(
        color = White99,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = tinkoffSansFamily,
        lineHeight = 27.sp
    ),
    bodyLarge = TextStyle(
        color = White99,
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = tinkoffSansFamily,
        lineHeight = 23.sp
    ),
    bodyMedium = TextStyle(
        color = White99,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = tinkoffSansFamily,
        lineHeight = 21.sp
    ),
    bodySmall = TextStyle(
        color = White99,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = tinkoffSansFamily,
        lineHeight = 19.sp
    ),
    labelLarge = TextStyle(
        color = White99,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = tinkoffSansFamily,
        lineHeight = 19.sp
    ),
    labelMedium = TextStyle(
        color = White99,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = tinkoffSansFamily,
        lineHeight = 17.sp
    ),
    labelSmall = TextStyle(
        color = White99,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        fontFamily = tinkoffSansFamily,
        lineHeight = 17.sp
    )
)