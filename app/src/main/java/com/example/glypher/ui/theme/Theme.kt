package com.example.glypher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color



data class GlyphColors(
    val background: Color,
    val text: Color,
    val warning: Color
)



val LocalGlyphColors = staticCompositionLocalOf {
    GlyphColors(
        background = lightBackground,
        text = defaultWhite,
        warning = warningRed
    )
}

@Composable
fun GlypherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        GlyphColors(
            background = lightBackground,
            text = defaultWhite,
            warning = warningRed
        )
    }
    else {
        GlyphColors(
            background = darkBackground,
            text = defaultWhite,
            warning = warningRed
        )

    }

    CompositionLocalProvider(LocalGlyphColors provides colorScheme) {
        content()
    }


}