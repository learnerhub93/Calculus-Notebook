package com.example

import com.example.data.CalculusCurriculum
import com.example.ui.components.math.LatexMathParser
import com.example.ui.components.math.MathNode
import org.junit.Assert.*
import org.junit.Test

class LatexMathParserTest {

    @Test
    fun `parses fraction correctly into MathNode Fraction`() {
        val nodes = LatexMathParser.parse("\\frac{\\Delta y}{\\Delta x}")
        assertTrue("Expected nodes to contain a Fraction", nodes.any { it is MathNode.Fraction })
        val frac = nodes.filterIsInstance<MathNode.Fraction>().first()
        assertTrue("Numerator should have nodes", frac.numerator.isNotEmpty())
        assertTrue("Denominator should have nodes", frac.denominator.isNotEmpty())
    }

    @Test
    fun `parses limit with subscript target`() {
        val nodes = LatexMathParser.parse("\\lim_{x \\to c} f(x)")
        assertTrue("Expected nodes to contain a Limit", nodes.any { it is MathNode.Limit })
        val limit = nodes.filterIsInstance<MathNode.Limit>().first()
        assertTrue("Subscript should contain approach variables", limit.sub.isNotEmpty())
    }

    @Test
    fun `parses integral with lower and upper bounds`() {
        val nodes = LatexMathParser.parse("\\int_a^b f(x)\\,dx")
        assertTrue("Expected nodes to contain an Integral", nodes.any { it is MathNode.Integral })
        val integral = nodes.filterIsInstance<MathNode.Integral>().first()
        assertNotNull("Lower bound should not be null", integral.lower)
        assertNotNull("Upper bound should not be null", integral.upper)
    }

    @Test
    fun `parses summation with index and infinity`() {
        val nodes = LatexMathParser.parse("\\sum_{n=0}^{\\infty} a_n")
        assertTrue("Expected nodes to contain a Summation", nodes.any { it is MathNode.Summation })
        val sum = nodes.filterIsInstance<MathNode.Summation>().first()
        assertNotNull("Lower bound should be present", sum.lower)
        assertNotNull("Upper bound should be present", sum.upper)
    }

    @Test
    fun `parses radicals correctly`() {
        val nodes = LatexMathParser.parse("\\sqrt{x^2 + 1}")
        assertTrue("Expected nodes to contain a Radical", nodes.any { it is MathNode.Radical })
        val radical = nodes.filterIsInstance<MathNode.Radical>().first()
        assertTrue("Radicand should not be empty", radical.radicand.isNotEmpty())
    }

    @Test
    fun `parses power and subscripts scripts`() {
        val nodes = LatexMathParser.parse("x^2 + y_0")
        assertTrue("Expected nodes to contain a Script", nodes.any { it is MathNode.Script })
    }

    @Test
    fun `all 25 topics in curriculum parse into valid math nodes without exception`() {
        val curriculum = CalculusCurriculum.topics
        assertEquals(25, curriculum.size)

        for (topic in curriculum) {
            val nodes = LatexMathParser.parse(topic.formulaLatex)
            assertNotNull("Parsed nodes should not be null for topic ${topic.id}", nodes)
            assertTrue("Parsed nodes should not be empty for topic ${topic.id}", nodes.isNotEmpty())

            // Also test example steps
            for (step in topic.exampleSteps) {
                val stepNodes = LatexMathParser.parse(step.mathExpression)
                assertNotNull("Step math expression should parse without error", stepNodes)
            }
        }
    }
}
