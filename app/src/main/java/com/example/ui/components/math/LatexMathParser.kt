package com.example.ui.components.math

object LatexMathParser {

    private val KNOWN_FUNCTIONS = setOf(
        "sin", "cos", "tan", "sec", "csc", "cot",
        "arcsin", "arccos", "arctan", "ln", "log",
        "exp", "lim", "max", "min", "det"
    )

    private val GREEK_OR_SYMBOLS = mapOf(
        "\\Delta" to "Δ",
        "\\delta" to "δ",
        "\\partial" to "∂",
        "\\infty" to "∞",
        "\\cdot" to "·",
        "\\times" to "×",
        "\\to" to "→",
        "\\rightarrow" to "→",
        "\\leftarrow" to "←",
        "\\implies" to "⟹",
        "\\iff" to "⟺",
        "\\pm" to "±",
        "\\approx" to "≈",
        "\\neq" to "≠",
        "\\le" to "≤",
        "\\ge" to "≥",
        "\\pi" to "π",
        "\\theta" to "θ",
        "\\alpha" to "α",
        "\\beta" to "β",
        "\\lambda" to "λ",
        "\\mu" to "μ",
        "\\sigma" to "σ",
        "\\nabla" to "∇",
        "\\in" to "∈",
        "\\forall" to "∀",
        "\\exists" to "∃"
    )

    fun parse(latex: String): List<MathNode> {
        val cleaned = preprocess(latex)
        val reader = CharReader(cleaned)
        return parseNodes(reader, stopOn = null)
    }

    private fun preprocess(input: String): String {
        var s = input.trim()
        // Replace common unicode or ASCII approximations into standard LaTeX
        s = s.replace("->", "\\to ")
        s = s.replace("<=>", "\\iff ")
        s = s.replace("=>", "\\implies ")
        s = s.replace("!=", "\\neq ")
        s = s.replace("<=", "\\le ")
        s = s.replace(">=", "\\ge ")
        return s
    }

    private fun parseNodes(reader: CharReader, stopOn: Char?): List<MathNode> {
        val nodes = mutableListOf<MathNode>()

        while (reader.hasNext()) {
            if (stopOn != null && reader.peek() == stopOn) {
                break
            }

            val c = reader.peek()

            when {
                c.isWhitespace() -> {
                    reader.next()
                    // Aggregate consecutive whitespace into a single Space node
                    while (reader.hasNext() && reader.peek().isWhitespace()) {
                        reader.next()
                    }
                    nodes.add(MathNode.Space(6))
                }

                c == '\\' -> {
                    val cmd = reader.readCommand()
                    handleCommand(cmd, reader, nodes)
                }

                c == '^' -> {
                    reader.next() // consume '^'
                    val expNodes = parseGroupOrSingle(reader)
                    if (nodes.isNotEmpty()) {
                        val last = nodes.removeAt(nodes.lastIndex)
                        if (last is MathNode.Script) {
                            nodes.add(last.copy(superscript = expNodes))
                        } else {
                            nodes.add(MathNode.Script(base = listOf(last), superscript = expNodes))
                        }
                    } else {
                        nodes.add(MathNode.Script(base = emptyList(), superscript = expNodes))
                    }
                }

                c == '_' -> {
                    reader.next() // consume '_'
                    val subNodes = parseGroupOrSingle(reader)
                    if (nodes.isNotEmpty()) {
                        val last = nodes.removeAt(nodes.lastIndex)
                        if (last is MathNode.Script) {
                            nodes.add(last.copy(subscript = subNodes))
                        } else {
                            nodes.add(MathNode.Script(base = listOf(last), subscript = subNodes))
                        }
                    } else {
                        nodes.add(MathNode.Script(base = emptyList(), subscript = subNodes))
                    }
                }

                c == '{' -> {
                    reader.next()
                    val inner = parseNodes(reader, stopOn = '}')
                    if (reader.hasNext() && reader.peek() == '}') reader.next()
                    nodes.addAll(inner)
                }

                c == '|' -> {
                    reader.next()
                    nodes.add(MathNode.Separator("|"))
                }

                c == '(' || c == '[' -> {
                    val open = c.toString()
                    val closeChar = if (c == '(') ')' else ']'
                    reader.next()
                    val inner = parseNodes(reader, stopOn = closeChar)
                    if (reader.hasNext() && reader.peek() == closeChar) {
                        reader.next()
                    }
                    nodes.add(MathNode.Bracketed(open, inner, closeChar.toString()))
                }

                else -> {
                    val token = reader.readToken()
                    if (token.isNotEmpty()) {
                        classifyToken(token, nodes)
                    }
                }
            }
        }

        return combineScripts(nodes)
    }

    private fun handleCommand(cmd: String, reader: CharReader, nodes: MutableList<MathNode>) {
        when (cmd) {
            "\\frac" -> {
                val num = parseRequiredGroup(reader)
                val den = parseRequiredGroup(reader)
                nodes.add(MathNode.Fraction(num, den))
            }

            "\\lim" -> {
                var subNodes: List<MathNode> = emptyList()
                reader.skipWhitespace()
                if (reader.hasNext() && reader.peek() == '_') {
                    reader.next() // consume '_'
                    subNodes = parseGroupOrSingle(reader)
                }
                nodes.add(MathNode.Limit(sub = subNodes, body = emptyList()))
            }

            "\\int" -> {
                var lower: List<MathNode>? = null
                var upper: List<MathNode>? = null

                // Parse potential _ and ^ in any order
                repeat(2) {
                    reader.skipWhitespace()
                    if (reader.hasNext() && reader.peek() == '_') {
                        reader.next()
                        lower = parseGroupOrSingle(reader)
                    } else if (reader.hasNext() && reader.peek() == '^') {
                        reader.next()
                        upper = parseGroupOrSingle(reader)
                    }
                }
                nodes.add(MathNode.Integral(lower = lower, upper = upper))
            }

            "\\sum" -> {
                var lower: List<MathNode>? = null
                var upper: List<MathNode>? = null

                repeat(2) {
                    reader.skipWhitespace()
                    if (reader.hasNext() && reader.peek() == '_') {
                        reader.next()
                        lower = parseGroupOrSingle(reader)
                    } else if (reader.hasNext() && reader.peek() == '^') {
                        reader.next()
                        upper = parseGroupOrSingle(reader)
                    }
                }
                nodes.add(MathNode.Summation(lower = lower, upper = upper))
            }

            "\\sqrt" -> {
                var degree: String? = null
                reader.skipWhitespace()
                if (reader.hasNext() && reader.peek() == '[') {
                    reader.next() // consume '['
                    degree = reader.readUntil(']')
                    if (reader.hasNext() && reader.peek() == ']') reader.next()
                }
                val radicand = parseRequiredGroup(reader)
                nodes.add(MathNode.Radical(degree, radicand))
            }

            "\\text", "\\mathrm", "\\operatorname" -> {
                val textContent = parseRawGroup(reader)
                nodes.add(MathNode.Plain(textContent, isTextMode = true))
            }

            "\\left" -> {
                reader.skipWhitespace()
                val bracket = if (reader.hasNext()) reader.next().toString() else "("
                val inner = parseNodes(reader, stopOn = null) // will break on \right
                nodes.add(MathNode.Bracketed(bracket, inner, ")"))
            }

            "\\right" -> {
                // Handled gracefully as closer
                if (reader.hasNext()) reader.next()
            }

            "\\Big|", "\\big|" -> {
                nodes.add(MathNode.Separator("|"))
            }

            "\\quad" -> nodes.add(MathNode.Space(14))
            "\\qquad" -> nodes.add(MathNode.Space(22))
            "\\,", "\\;" -> nodes.add(MathNode.Space(4))

            else -> {
                val mapped = GREEK_OR_SYMBOLS[cmd]
                if (mapped != null) {
                    nodes.add(MathNode.Plain(mapped, isOperator = true))
                } else {
                    // Check if it's a known function like \sin, \cos, \ln
                    val funcName = cmd.removePrefix("\\")
                    if (KNOWN_FUNCTIONS.contains(funcName)) {
                        nodes.add(MathNode.Plain(funcName, isTextMode = true))
                    } else {
                        // Unknown command: render cleaned name
                        nodes.add(MathNode.Plain(funcName, isTextMode = false))
                    }
                }
            }
        }
    }

    private fun parseRequiredGroup(reader: CharReader): List<MathNode> {
        reader.skipWhitespace()
        return if (reader.hasNext() && reader.peek() == '{') {
            reader.next()
            val nodes = parseNodes(reader, stopOn = '}')
            if (reader.hasNext() && reader.peek() == '}') reader.next()
            nodes
        } else {
            parseGroupOrSingle(reader)
        }
    }

    private fun parseRawGroup(reader: CharReader): String {
        reader.skipWhitespace()
        if (!reader.hasNext()) return ""
        return if (reader.peek() == '{') {
            reader.next()
            val text = reader.readUntil('}')
            if (reader.hasNext() && reader.peek() == '}') reader.next()
            text
        } else {
            reader.next().toString()
        }
    }

    private fun parseGroupOrSingle(reader: CharReader): List<MathNode> {
        reader.skipWhitespace()
        if (!reader.hasNext()) return emptyList()

        return if (reader.peek() == '{') {
            reader.next()
            val inner = parseNodes(reader, stopOn = '}')
            if (reader.hasNext() && reader.peek() == '}') reader.next()
            inner
        } else {
            val c = reader.next()
            listOf(MathNode.Plain(c.toString(), isVariable = c.isLetter()))
        }
    }

    private fun classifyToken(token: String, nodes: MutableList<MathNode>) {
        if (token.all { it.isDigit() || it == '.' }) {
            nodes.add(MathNode.Plain(token, isOperator = false))
            return
        }

        if (token.length == 1 && token[0].isLetter()) {
            nodes.add(MathNode.Plain(token, isVariable = true))
            return
        }

        if (KNOWN_FUNCTIONS.contains(token)) {
            nodes.add(MathNode.Plain(token, isTextMode = true))
            return
        }

        if (token in listOf("+", "-", "=", "≠", "±", "·", "*", "<", ">", "≤", "≥", "→", "⟹", "⟺")) {
            nodes.add(MathNode.Plain(token, isOperator = true))
            return
        }

        // Differential markers like dx, dt, du, dy
        if (token.length == 2 && token[0] == 'd' && token[1].isLetter()) {
            nodes.add(MathNode.Plain(token, isTextMode = true))
            return
        }

        // General sequence: split single letters and operators
        var i = 0
        while (i < token.length) {
            val ch = token[i]
            when {
                ch.isLetter() -> {
                    nodes.add(MathNode.Plain(ch.toString(), isVariable = true))
                    i++
                }
                ch.isDigit() -> {
                    val start = i
                    while (i < token.length && (token[i].isDigit() || token[i] == '.')) i++
                    nodes.add(MathNode.Plain(token.substring(start, i), isOperator = false))
                }
                else -> {
                    nodes.add(MathNode.Plain(ch.toString(), isOperator = true))
                    i++
                }
            }
        }
    }

    private fun combineScripts(nodes: List<MathNode>): List<MathNode> {
        // Post-processing if any loose scripts need binding
        return nodes
    }

    private class CharReader(private val text: String) {
        private var index = 0

        fun hasNext(): Boolean = index < text.length
        fun peek(): Char = text[index]

        fun next(): Char {
            val c = text[index]
            index++
            return c
        }

        fun skipWhitespace() {
            while (hasNext() && peek().isWhitespace()) {
                index++
            }
        }

        fun readCommand(): String {
            val sb = StringBuilder()
            sb.append(next()) // the '\'
            while (hasNext() && (peek().isLetter() || peek() == '|')) {
                sb.append(next())
            }
            return sb.toString()
        }

        fun readToken(): String {
            val sb = StringBuilder()
            while (hasNext()) {
                val c = peek()
                if (c.isWhitespace() || c == '\\' || c == '^' || c == '_' || c == '{' || c == '}' ||
                    c == '(' || c == ')' || c == '[' || c == ']' || c == '|'
                ) {
                    break
                }
                sb.append(next())
            }
            return sb.toString()
        }

        fun readUntil(stopChar: Char): String {
            val sb = StringBuilder()
            var depth = 0
            while (hasNext()) {
                val c = peek()
                if (c == '{') depth++
                else if (c == '}') {
                    if (depth > 0) depth--
                    else if (stopChar == '}') break
                } else if (c == stopChar && depth == 0) {
                    break
                }
                sb.append(next())
            }
            return sb.toString()
        }
    }
}
