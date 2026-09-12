package com.example

import com.example.ui.components.CanvasBackground
import com.example.ui.components.CanvasPoint
import com.example.ui.components.CanvasSerializationHelper
import com.example.ui.components.CanvasStroke
import com.example.ui.components.CanvasTool
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculusScratchpadTest {

    @Test
    fun `stroke serialization and deserialization preserves points and properties`() {
        val originalStrokes = listOf(
            CanvasStroke(
                points = listOf(CanvasPoint(10f, 20f), CanvasPoint(30f, 40f), CanvasPoint(50f, 60f)),
                colorInt = -16776961, // Blue
                strokeWidth = 4.5f,
                isHighlighter = false,
                isStraightLine = true
            ),
            CanvasStroke(
                points = listOf(CanvasPoint(100f, 150f), CanvasPoint(120f, 180f)),
                colorInt = -65536, // Red
                strokeWidth = 14f,
                isHighlighter = true,
                isStraightLine = false
            )
        )

        val serialized = CanvasSerializationHelper.serializeStrokes(originalStrokes)
        assertTrue(serialized.isNotEmpty())
        assertTrue(serialized.contains("10"))
        assertTrue(serialized.contains("40"))

        val deserialized = CanvasSerializationHelper.deserializeStrokes(serialized)
        assertEquals(2, deserialized.size)

        // Verify stroke 1
        val s1 = deserialized[0]
        assertEquals(3, s1.points.size)
        assertEquals(10f, s1.points[0].x, 0.01f)
        assertEquals(20f, s1.points[0].y, 0.01f)
        assertEquals(30f, s1.points[1].x, 0.01f)
        assertEquals(40f, s1.points[1].y, 0.01f)
        assertEquals(-16776961, s1.colorInt)
        assertEquals(4.5f, s1.strokeWidth, 0.01f)
        assertFalse(s1.isHighlighter)
        assertTrue(s1.isStraightLine)

        // Verify stroke 2
        val s2 = deserialized[1]
        assertEquals(2, s2.points.size)
        assertEquals(100f, s2.points[0].x, 0.01f)
        assertEquals(150f, s2.points[0].y, 0.01f)
        assertEquals(-65536, s2.colorInt)
        assertEquals(14f, s2.strokeWidth, 0.01f)
        assertTrue(s2.isHighlighter)
        assertFalse(s2.isStraightLine)
    }

    @Test
    fun `deserialize handles empty and invalid data gracefully`() {
        val emptyList1 = CanvasSerializationHelper.deserializeStrokes(null)
        assertTrue(emptyList1.isEmpty())

        val emptyList2 = CanvasSerializationHelper.deserializeStrokes("")
        assertTrue(emptyList2.isEmpty())

        val emptyList3 = CanvasSerializationHelper.deserializeStrokes("invalid json data {[]}")
        assertTrue(emptyList3.isEmpty())
    }

    @Test
    fun `canvas tools and backgrounds have complete options`() {
        assertEquals(4, CanvasTool.values().size)
        assertTrue(CanvasTool.values().contains(CanvasTool.PEN))
        assertTrue(CanvasTool.values().contains(CanvasTool.HIGHLIGHTER))
        assertTrue(CanvasTool.values().contains(CanvasTool.LINE))
        assertTrue(CanvasTool.values().contains(CanvasTool.ERASER))

        assertEquals(4, CanvasBackground.values().size)
        assertTrue(CanvasBackground.values().contains(CanvasBackground.CARTESIAN))
        assertTrue(CanvasBackground.values().contains(CanvasBackground.LINED))
        assertTrue(CanvasBackground.values().contains(CanvasBackground.DOTS))
        assertTrue(CanvasBackground.values().contains(CanvasBackground.BLANK))
    }
}
