package com.example.ui.components.math

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.*
import com.example.ui.theme.*

/**
 * LatexMathView: A custom Jetpack Compose mathematical equation layout engine
 * that formats and renders LaTeX expressions cleanly and natively.
 */
@Composable
fun LatexMathView(
    latex: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 17.sp,
    textColor: Color = InkNavy,
    mathFontFamily: FontFamily = FontFamily.Serif
) {
    val nodes = remember(latex) {
        LatexMathParser.parse(latex)
    }

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        RenderMathNodes(
            nodes = nodes,
            fontSize = fontSize,
            color = textColor,
            mathFontFamily = mathFontFamily
        )
    }
}

@Composable
fun RenderMathNodes(
    nodes: List<MathNode>,
    fontSize: TextUnit,
    color: Color,
    mathFontFamily: FontFamily
) {
    nodes.forEach { node ->
        RenderSingleMathNode(
            node = node,
            fontSize = fontSize,
            color = color,
            mathFontFamily = mathFontFamily
        )
    }
}

@Composable
private fun RenderSingleMathNode(
    node: MathNode,
    fontSize: TextUnit,
    color: Color,
    mathFontFamily: FontFamily
) {
    when (node) {
        is MathNode.Plain -> {
            Text(
                text = node.text,
                fontSize = fontSize,
                fontWeight = if (node.isOperator) FontWeight.SemiBold else FontWeight.Normal,
                fontStyle = if (node.isVariable) FontStyle.Italic else FontStyle.Normal,
                fontFamily = if (node.isTextMode) FontFamily.Default else mathFontFamily,
                color = color,
                modifier = Modifier.padding(horizontal = if (node.isOperator) 2.dp else 0.5.dp)
            )
        }

        is MathNode.Fraction -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                // Numerator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    RenderMathNodes(
                        nodes = node.numerator,
                        fontSize = (fontSize.value * 0.90f).sp,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }

                // Fraction Bar
                Box(
                    modifier = Modifier
                        .height(1.4.dp)
                        .widthIn(min = 16.dp)
                        .fillMaxWidth()
                        .background(color)
                )

                // Denominator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    RenderMathNodes(
                        nodes = node.denominator,
                        fontSize = (fontSize.value * 0.90f).sp,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }
            }
        }

        is MathNode.Limit -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 3.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "lim",
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        fontFamily = mathFontFamily,
                        color = color
                    )
                    if (node.sub.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.offset(y = (-1).dp)
                        ) {
                            RenderMathNodes(
                                nodes = node.sub,
                                fontSize = (fontSize.value * 0.68f).sp,
                                color = color.copy(alpha = 0.92f),
                                mathFontFamily = mathFontFamily
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(3.dp))
                if (node.body.isNotEmpty()) {
                    RenderMathNodes(
                        nodes = node.body,
                        fontSize = fontSize,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }
            }
        }

        is MathNode.Integral -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                Text(
                    text = "∫",
                    fontSize = (fontSize.value * 1.55f).sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    color = color,
                    modifier = Modifier.offset(y = (-1).dp)
                )

                // Limits next to integral sign
                if (node.lower != null || node.upper != null) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .height(28.dp)
                            .padding(end = 2.dp)
                    ) {
                        if (node.upper != null) {
                            Row {
                                RenderMathNodes(
                                    nodes = node.upper,
                                    fontSize = (fontSize.value * 0.65f).sp,
                                    color = color,
                                    mathFontFamily = mathFontFamily
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(1.dp))
                        }

                        if (node.lower != null) {
                            Row {
                                RenderMathNodes(
                                    nodes = node.lower,
                                    fontSize = (fontSize.value * 0.65f).sp,
                                    color = color,
                                    mathFontFamily = mathFontFamily
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(1.dp))
                        }
                    }
                }

                if (node.integrand.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(2.dp))
                    RenderMathNodes(
                        nodes = node.integrand,
                        fontSize = fontSize,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }
            }
        }

        is MathNode.Summation -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (node.upper != null) {
                        Row {
                            RenderMathNodes(
                                nodes = node.upper,
                                fontSize = (fontSize.value * 0.65f).sp,
                                color = color,
                                mathFontFamily = mathFontFamily
                            )
                        }
                    }
                    Text(
                        text = "Σ",
                        fontSize = (fontSize.value * 1.35f).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = color
                    )
                    if (node.lower != null) {
                        Row {
                            RenderMathNodes(
                                nodes = node.lower,
                                fontSize = (fontSize.value * 0.65f).sp,
                                color = color,
                                mathFontFamily = mathFontFamily
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(3.dp))
                if (node.term.isNotEmpty()) {
                    RenderMathNodes(
                        nodes = node.term,
                        fontSize = fontSize,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }
            }
        }

        is MathNode.Radical -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                Text(
                    text = "√",
                    fontSize = (fontSize.value * 1.35f).sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Serif,
                    color = color
                )
                Column(
                    modifier = Modifier.padding(start = 1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(1.3.dp)
                            .fillMaxWidth()
                            .background(color)
                    )
                    Row(
                        modifier = Modifier.padding(top = 1.dp, start = 2.dp, end = 2.dp)
                    ) {
                        RenderMathNodes(
                            nodes = node.radicand,
                            fontSize = fontSize,
                            color = color,
                            mathFontFamily = mathFontFamily
                        )
                    }
                }
            }
        }

        is MathNode.Script -> {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (node.base.isNotEmpty()) {
                    RenderMathNodes(
                        nodes = node.base,
                        fontSize = fontSize,
                        color = color,
                        mathFontFamily = mathFontFamily
                    )
                }

                if (node.superscript != null || node.subscript != null) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 1.dp)
                    ) {
                        if (node.superscript != null) {
                            Row(modifier = Modifier.offset(y = (-5).dp)) {
                                RenderMathNodes(
                                    nodes = node.superscript,
                                    fontSize = (fontSize.value * 0.72f).sp,
                                    color = color,
                                    mathFontFamily = mathFontFamily
                                )
                            }
                        }
                        if (node.subscript != null) {
                            Row(modifier = Modifier.offset(y = 4.dp)) {
                                RenderMathNodes(
                                    nodes = node.subscript,
                                    fontSize = (fontSize.value * 0.72f).sp,
                                    color = color,
                                    mathFontFamily = mathFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }

        is MathNode.Bracketed -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 1.5.dp)
            ) {
                Text(
                    text = node.open,
                    fontSize = (fontSize.value * 1.15f).sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Serif,
                    color = color
                )
                RenderMathNodes(
                    nodes = node.content,
                    fontSize = fontSize,
                    color = color,
                    mathFontFamily = mathFontFamily
                )
                Text(
                    text = node.close,
                    fontSize = (fontSize.value * 1.15f).sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.Serif,
                    color = color
                )
            }
        }

        is MathNode.Separator -> {
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(20.dp)
                    .width(1.2.dp)
                    .background(color.copy(alpha = 0.35f))
            )
        }

        is MathNode.Space -> {
            Spacer(modifier = Modifier.width(node.widthDp.dp))
        }
    }
}

/**
 * LatexFormulaCard: A comprehensive notebook formula card with:
 * - Beautifully rendered LaTeX equations
 * - Interactive toggle between "Rendered Math" and "LaTeX Source Code"
 * - 1-tap "Copy LaTeX" for students
 * - "Inspect Formula" dialog explaining key mathematical symbols
 */
@Composable
fun LatexFormulaCard(
    title: String,
    latex: String,
    explanation: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val writingStyle = LocalWritingStyle.current
    var showRawLatex by remember { mutableStateOf(false) }
    var showInspectDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(2.dp, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFFFEEA) // Classic notebook yellow sticky paper
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header: Title + Toolbar (Toggle LaTeX / Inspect / Copy)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Formula Title with highlighter tag
                    Box(
                        modifier = Modifier
                            .background(HighlighterYellow, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "★ $title",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = writingStyle.headingFontFamily
                        )
                    }

                    // Action buttons (TeX toggle, Zoom, Copy)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Toggle Rendered vs LaTeX Code
                        IconButton(
                            onClick = { showRawLatex = !showRawLatex },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (showRawLatex) Icons.Default.Functions else Icons.Default.Code,
                                contentDescription = if (showRawLatex) "Show Rendered Math" else "Show LaTeX Code",
                                tint = InkNavy,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Inspect formula dialog
                        IconButton(
                            onClick = { showInspectDialog = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Inspect Formula Breakdown",
                                tint = InkNavy,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Copy LaTeX button
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("LaTeX Equation", latex)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "LaTeX equation copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy LaTeX Equation",
                                tint = InkNavy,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // The mathematical expression displayed clearly in a prominent card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showRawLatex) {
                            // Raw LaTeX Source with syntax style
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "LaTeX Source:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6B7280)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(3.dp),
                                        color = Color(0xFFEFF6FF)
                                    ) {
                                        Text(
                                            text = "TeX",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2563EB),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = latex,
                                    fontSize = 12.5.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 18.sp
                                )
                            }
                        } else {
                            // High-Fidelity Custom LaTeX Math Rendering
                            LatexMathView(
                                latex = latex,
                                fontSize = 18.sp,
                                textColor = InkNavy,
                                mathFontFamily = writingStyle.mathFontFamily
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Plain-language breakdown of components
                Text(
                    text = explanation,
                    fontSize = 12.5.sp,
                    color = InkCharcoal,
                    fontStyle = FontStyle.Italic,
                    fontFamily = writingStyle.bodyFontFamily,
                    lineHeight = 18.sp
                )
            }
        }
    }

    // Inspect Formula Breakdown Dialog
    if (showInspectDialog) {
        Dialog(onDismissRequest = { showInspectDialog = false }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PaperCream,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Formula Anatomy",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkNavy,
                        fontFamily = writingStyle.headingFontFamily
                    )
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = InkCharcoal
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Enlarged equation display
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LatexMathView(
                                latex = latex,
                                fontSize = 22.sp,
                                textColor = InkNavy,
                                mathFontFamily = writingStyle.mathFontFamily
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // LaTeX code block with copy button
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "LaTeX Code (for Overleaf / Papers):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = latex,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = explanation,
                        fontSize = 12.sp,
                        color = InkDark,
                        lineHeight = 17.sp,
                        fontFamily = writingStyle.bodyFontFamily
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("LaTeX Equation", latex)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "LaTeX copied!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy TeX", fontSize = 11.5.sp)
                        }

                        Button(
                            onClick = { showInspectDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
                        ) {
                            Text("Got it", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
