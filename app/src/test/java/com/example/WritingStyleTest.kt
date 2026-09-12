package com.example

import com.example.model.WritingStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WritingStyleTest {

    @Test
    fun `default writing style is Classic Notebook Serif`() {
        assertEquals(WritingStyle.SERIF, WritingStyle.DEFAULT)
        assertEquals("serif", WritingStyle.DEFAULT.id)
    }

    @Test
    fun `all writing styles have non-empty metadata`() {
        WritingStyle.entries.forEach { style ->
            assertNotNull(style.id)
            assertNotNull(style.displayName)
            assertNotNull(style.toolName)
            assertNotNull(style.description)
            assertNotNull(style.sampleSnippet)
            assertNotNull(style.headingFontFamily)
            assertNotNull(style.bodyFontFamily)
            assertNotNull(style.mathFontFamily)
            assertNotNull(style.noteFontFamily)
        }
    }

    @Test
    fun `fromId retrieves correct writing style with fallback`() {
        assertEquals(WritingStyle.CURSIVE, WritingStyle.fromId("cursive"))
        assertEquals(WritingStyle.SERIF, WritingStyle.fromId("serif"))
        assertEquals(WritingStyle.NEAT_PRINT, WritingStyle.fromId("neat_print"))
        assertEquals(WritingStyle.TECHNICAL_MONO, WritingStyle.fromId("technical_mono"))
        assertEquals(WritingStyle.LECTURE_HYBRID, WritingStyle.fromId("lecture_hybrid"))
        // Unknown id falls back to DEFAULT
        assertEquals(WritingStyle.DEFAULT, WritingStyle.fromId("unknown_style_id"))
        assertEquals(WritingStyle.DEFAULT, WritingStyle.fromId(null))
    }
}
