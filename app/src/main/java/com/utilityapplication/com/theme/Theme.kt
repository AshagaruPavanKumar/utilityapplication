package com.utilityapplication.com.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.utilityapplication.com.core.prefs.AppPreferences

private fun lightScheme(primary: Color) = lightColorScheme(
    primary = primary,
    onPrimary = Color.White,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

private fun darkScheme(primary: Color) = darkColorScheme(
    primary = primary,
    onPrimary = Color.White,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

@Composable
fun UtilityKitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeColor: String = AppPreferences.COLOR_BLUE,
    content: @Composable () -> Unit
) {
    val primary = themePrimary(themeColor)
    val colorScheme = if (darkTheme) darkScheme(primary) else lightScheme(primary)
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun UtilityAppTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { AppPreferences.get(context) }
    val dark by prefs.darkMode.collectAsState()
    val color by prefs.themeColor.collectAsState()
    UtilityKitTheme(darkTheme = dark, themeColor = color, content = content)
}