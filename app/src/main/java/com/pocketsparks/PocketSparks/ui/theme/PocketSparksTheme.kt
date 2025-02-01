package com.pocketsparks.PocketSparks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.pocketsparks.PocketSparks.ui.theme.spark.SparkDarkColorScheme
import com.pocketsparks.PocketSparks.ui.theme.spark.SparkLightColorScheme
import com.pocketsparks.PocketSparks.ui.theme.spark.SparkTypography

@Composable
fun PocketSparksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> SparkDarkColorScheme
        else -> SparkLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = SparkTypography, content = content
    )
}