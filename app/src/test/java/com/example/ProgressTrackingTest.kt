package com.example

import com.example.data.CalculusCurriculum
import com.example.ui.components.SidebarFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressTrackingTest {

    @Test
    fun `curriculum contains exactly 25 topics across 5 parts`() {
        assertEquals(25, CalculusCurriculum.topics.size)
        assertEquals(5, CalculusCurriculum.parts.size)

        val totalTopicsInParts = CalculusCurriculum.parts.sumOf { it.topicIds.size }
        assertEquals(25, totalTopicsInParts)
    }

    @Test
    fun `sidebar filter enumerations are defined`() {
        assertEquals(3, SidebarFilter.entries.size)
        assertTrue(SidebarFilter.entries.contains(SidebarFilter.ALL))
        assertTrue(SidebarFilter.entries.contains(SidebarFilter.COMPLETED))
        assertTrue(SidebarFilter.entries.contains(SidebarFilter.REMAINING))
    }

    @Test
    fun `completion fraction calculations are accurate`() {
        val completedTopics = setOf(1, 2, 3, 4, 5)
        val totalTopics = CalculusCurriculum.topics.size
        val fraction = completedTopics.size.toFloat() / totalTopics.toFloat()

        assertEquals(0.20f, fraction, 0.001f)
        assertEquals(20, (fraction * 100).toInt())

        // Part 1 has 4 topics (1..4) and all 4 are completed
        val part1Topics = CalculusCurriculum.topics.filter { it.partId == 1 }
        assertEquals(4, part1Topics.size)
        val part1Done = part1Topics.count { completedTopics.contains(it.id) }
        assertEquals(4, part1Done)
        assertEquals(part1Topics.size, part1Done)
    }
}
