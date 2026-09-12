package com.example.model

import androidx.compose.ui.text.font.FontFamily

/**
 * Represents the handwriting / writing style font configuration for the Calculus Notebook.
 */
enum class WritingStyle(
    val id: String,
    val displayName: String,
    val categoryName: String,
    val description: String,
    val icon: String,
    val headingFontFamily: FontFamily,
    val bodyFontFamily: FontFamily,
    val mathFontFamily: FontFamily,
    val noteFontFamily: FontFamily,
    val sampleSnippet: String,
    val toolName: String
) {
    CURSIVE(
        id = "cursive",
        displayName = "Teacher Cursive",
        categoryName = "Handwritten Script",
        description = "Flowing, cursive script reminiscent of a calculus professor's chalkboard proofs.",
        icon = "✒️",
        headingFontFamily = FontFamily.Cursive,
        bodyFontFamily = FontFamily.Cursive,
        mathFontFamily = FontFamily.Cursive,
        noteFontFamily = FontFamily.Cursive,
        sampleSnippet = "f'(x) = lim h→0 [f(x+h) - f(x)] / h",
        toolName = "Fountain Pen"
    ),
    SERIF(
        id = "serif",
        displayName = "Classic Notebook Serif",
        categoryName = "Academic Nib",
        description = "Traditional academic typography with elegant, high-contrast serifs.",
        icon = "🖋️",
        headingFontFamily = FontFamily.Serif,
        bodyFontFamily = FontFamily.Serif,
        mathFontFamily = FontFamily.Serif,
        noteFontFamily = FontFamily.Serif,
        sampleSnippet = "∫ f(x) dx = F(b) - F(a)",
        toolName = "Calligraphy Nib"
    ),
    NEAT_PRINT(
        id = "neat_print",
        displayName = "Student Neat Print",
        categoryName = "Ballpoint Pen",
        description = "Clean, highly legible modern handwriting for swift reading and revision.",
        icon = "✏️",
        headingFontFamily = FontFamily.SansSerif,
        bodyFontFamily = FontFamily.SansSerif,
        mathFontFamily = FontFamily.Monospace,
        noteFontFamily = FontFamily.SansSerif,
        sampleSnippet = "d/dx [xⁿ] = n · xⁿ⁻¹",
        toolName = "0.5mm Gel Pen"
    ),
    TECHNICAL_MONO(
        id = "technical_mono",
        displayName = "Engineer Monospace",
        categoryName = "Graph Paper / Typewriter",
        description = "Even-width technical drafting font for computer scientists and engineers.",
        icon = "📐",
        headingFontFamily = FontFamily.Monospace,
        bodyFontFamily = FontFamily.Monospace,
        mathFontFamily = FontFamily.Monospace,
        noteFontFamily = FontFamily.Monospace,
        sampleSnippet = "lim (x→c) f(x) = L  <=>  ∀ε>0 ∃δ>0",
        toolName = "Technical Drafting Pen"
    ),
    LECTURE_HYBRID(
        id = "lecture_hybrid",
        displayName = "Chalkboard & Notes",
        categoryName = "Curated Mix",
        description = "Cursive section headers paired with crisp print intuition and monospace equations.",
        icon = "📋",
        headingFontFamily = FontFamily.Cursive,
        bodyFontFamily = FontFamily.SansSerif,
        mathFontFamily = FontFamily.Monospace,
        noteFontFamily = FontFamily.Serif,
        sampleSnippet = "Slope of secant line → Tangent slope as h → 0",
        toolName = "Chalk & Pen Duo"
    );

    companion object {
        val DEFAULT = SERIF

        fun fromId(id: String?): WritingStyle {
            return entries.find { it.id == id } ?: DEFAULT
        }
    }
}
