package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.model.WritingStyle

val LocalWritingStyle = compositionLocalOf { WritingStyle.DEFAULT }

private val NotebookColorScheme = lightColorScheme(
    primary = NotebookInkNavy,
    onPrimary = Color.White,
    primaryContainer = NotebookCreamDark,
    onPrimaryContainer = NotebookInkNavy,
    secondary = NotebookInkDark,
    onSecondary = Color.White,
    tertiary = NotebookCorrectionRed,
    background = NotebookCream,
    onBackground = NotebookInkDark,
    surface = NotebookCream,
    onSurface = NotebookInkDark,
    surfaceVariant = NotebookCreamDark,
    onSurfaceVariant = NotebookInkDark
)

@Composable
fun MyApplicationTheme(
    writingStyle: WritingStyle = WritingStyle.DEFAULT,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalWritingStyle provides writingStyle) {
        MaterialTheme(
            colorScheme = NotebookColorScheme,
            typography = getNotebookTypography(writingStyle),
            content = content
        )
    }
}

