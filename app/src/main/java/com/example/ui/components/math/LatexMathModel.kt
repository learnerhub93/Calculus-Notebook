package com.example.ui.components.math

/**
 * Abstract Syntax Tree representation of a mathematical formula for LaTeX rendering.
 */
sealed interface MathNode {
    data class Plain(
        val text: String,
        val isVariable: Boolean = false,
        val isTextMode: Boolean = false,
        val isOperator: Boolean = false
    ) : MathNode

    data class Fraction(
        val numerator: List<MathNode>,
        val denominator: List<MathNode>
    ) : MathNode

    data class Limit(
        val sub: List<MathNode>,
        val body: List<MathNode>
    ) : MathNode

    data class Integral(
        val lower: List<MathNode>? = null,
        val upper: List<MathNode>? = null,
        val integrand: List<MathNode> = emptyList()
    ) : MathNode

    data class Summation(
        val lower: List<MathNode>? = null,
        val upper: List<MathNode>? = null,
        val term: List<MathNode> = emptyList()
    ) : MathNode

    data class Radical(
        val degree: String? = null,
        val radicand: List<MathNode>
    ) : MathNode

    data class Script(
        val base: List<MathNode>,
        val superscript: List<MathNode>? = null,
        val subscript: List<MathNode>? = null
    ) : MathNode

    data class Bracketed(
        val open: String,
        val content: List<MathNode>,
        val close: String
    ) : MathNode

    data class Separator(val text: String = "|") : MathNode
    data class Space(val widthDp: Int = 6) : MathNode
}
