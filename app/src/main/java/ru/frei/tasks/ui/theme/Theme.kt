package ru.frei.tasks.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPrimary = Color(0xFF303030)
private val ColorAccent = Color(0xFFD0D0D0)

private val DarkColorScheme = darkColorScheme(
    primary = ColorPrimary,
    primaryContainer = ColorPrimary,
    secondary = ColorAccent,
    secondaryContainer = ColorAccent,
    background = ColorPrimary,
    surface = ColorPrimary,
    onPrimary = ColorAccent,
    onSecondary = ColorPrimary,
    onBackground = ColorAccent,
    onSurface = ColorAccent
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}