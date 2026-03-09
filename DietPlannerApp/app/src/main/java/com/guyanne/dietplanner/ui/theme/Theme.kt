package com.guyanne.dietplanner.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Green80 = Color(0xFF66BB6A)
val Green40 = Color(0xFF2E7D32)
val Teal80 = Color(0xFF4DB6AC)
val Orange80 = Color(0xFFFFB74D)

private val LightColors = lightColorScheme(
    primary = Green40,
    onPrimary = Color.White,
    secondary = Teal80,
    tertiary = Orange80,
    background = Color(0xFFF9FBE7),
    surface = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = Green80,
    onPrimary = Color.Black,
    secondary = Teal80,
    tertiary = Orange80,
)

@Composable
fun DietPlannerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
