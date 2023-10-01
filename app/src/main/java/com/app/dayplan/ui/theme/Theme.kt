package com.app.dayplan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.app.dayplan.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/
)

val customFontFamily = FontFamily(Font(R.font.bmpro))

val customTypography = Typography().copy(
    displayLarge = Typography().displayLarge.copy(fontFamily = customFontFamily),
    displayMedium = Typography().displayMedium.copy(fontFamily = customFontFamily),
    displaySmall = Typography().displaySmall.copy(fontFamily = customFontFamily),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = customFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = customFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = customFontFamily),
    titleLarge = Typography().titleLarge.copy(fontFamily = customFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = customFontFamily),
    titleSmall = Typography().titleSmall.copy(fontFamily = customFontFamily),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = customFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = customFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = customFontFamily),
    labelLarge = Typography().labelLarge.copy(fontFamily = customFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = customFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = customFontFamily),
)

@Composable
fun DayplanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography,
        content = content
    )
}