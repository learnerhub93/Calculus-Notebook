package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalWritingStyle
import com.example.ui.components.math.LatexFormulaCard
import kotlin.random.Random

// Notebook Paper Color Palette
val PaperCream = Color(0xFFFDFBF7)
val PaperCreamDark = Color(0xFFF6F0E0)
val RuledBlue = Color(0xFFDCE5F2)
val MarginRed = Color(0xFFF87171)
val MarginRedLight = Color(0x33F87171)
val InkDark = Color(0xFF1E293B)
val InkNavy = Color(0xFF1E3A8A)
val InkCharcoal = Color(0xFF334155)
val PencilLead = Color(0xFF475569)
val CorrectionRed = Color(0xFFDC2626)
val HighlighterYellow = Color(0x70FEF08A)
val HighlighterYellowSolid = Color(0xFFFEF08A)
val HighlighterGreen = Color(0x5586EFAC)
val StickyYellow = Color(0xFFFEF9C3)
val StickyTape = Color(0x77E2E8F0)
val SpiralMetal = Color(0xFF94A3B8)
val SpiralHole = Color(0xFFE2E8F0)

/**
 * Ruled Notebook Paper Background Canvas.
 * Draws subtle horizontal blue ruled lines and a vertical red margin line,
 * plus spiral notebook holes on the far left.
 */
@Composable
fun NotebookPaperBackground(
    modifier: Modifier = Modifier,
    lineSpacingDp: Dp = 28.dp,
    marginOffsetDp: Dp = 0.dp,
    showSpiral: Boolean = false,
    showMarginLine: Boolean = false
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val lineSpacing = lineSpacingDp.toPx()
        val marginOffset = marginOffsetDp.toPx()

        // 1. Draw ruled horizontal blue lines
        var y = lineSpacing
        while (y < height) {
            drawLine(
                color = RuledBlue,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += lineSpacing
        }

        // 2. Draw vertical margin red line (only if explicitly enabled)
        if (showMarginLine && marginOffset > 0f) {
            drawLine(
                color = MarginRed,
                start = Offset(marginOffset, 0f),
                end = Offset(marginOffset, height),
                strokeWidth = 1.8f
            )
            drawLine(
                color = MarginRedLight,
                start = Offset(marginOffset - 4f, 0f),
                end = Offset(marginOffset - 4f, height),
                strokeWidth = 0.8f
            )
        }

        // 3. Draw spiral holes along the left edge (only if explicitly enabled)
        if (showSpiral) {
            var holeY = lineSpacing * 1.5f
            while (holeY < height) {
                drawCircle(
                    color = SpiralHole,
                    radius = 5.dp.toPx(),
                    center = Offset(14.dp.toPx(), holeY)
                )
                val coilPath = Path().apply {
                    moveTo(0f, holeY - 4f)
                    cubicTo(
                        10.dp.toPx(), holeY - 6f,
                        18.dp.toPx(), holeY + 2f,
                        14.dp.toPx(), holeY + 4f
                    )
                }
                drawPath(
                    path = coilPath,
                    color = SpiralMetal,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                )
                holeY += lineSpacing * 2.5f
            }
        }
    }
}

/**
 * Hand-Drawn / Wobbly Box.
 * Generates an organic, sketched rectangular border with slightly offset vertices.
 */
@Composable
fun HandDrawnBox(
    modifier: Modifier = Modifier,
    strokeColor: Color = InkDark,
    strokeWidthDp: Dp = 2.dp,
    fillColor: Color = Color.Transparent,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val sw = strokeWidthDp.toPx()

            // Generate deterministic yet organic wobbly outline
            val path = Path().apply {
                moveTo(6f, 3f)
                // Top edge with slight dip
                cubicTo(w * 0.33f, 1f, w * 0.66f, 4f, w - 4f, 3f)
                // Right edge with slight wave
                cubicTo(w - 2f, h * 0.33f, w - 5f, h * 0.66f, w - 3f, h - 4f)
                // Bottom edge
                cubicTo(w * 0.66f, h - 2f, w * 0.33f, h - 5f, 4f, h - 3f)
                // Left edge
                cubicTo(6f, h * 0.66f, 2f, h * 0.33f, 6f, 3f)
                close()
            }

            if (fillColor != Color.Transparent) {
                drawPath(path = path, color = fillColor)
            }

            drawPath(
                path = path,
                color = strokeColor,
                style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Double sketchy accent stroke on one corner
            val accentPath = Path().apply {
                moveTo(w - 12f, h - 4f)
                lineTo(w - 3f, h - 4f)
                lineTo(w - 3f, h - 14f)
            }
            drawPath(
                path = accentPath,
                color = strokeColor.copy(alpha = 0.6f),
                style = Stroke(width = sw * 0.8f, cap = StrokeCap.Round)
            )
        }
        content()
    }
}

/**
 * Highlighter Box for important formulas and key terms with native LaTeX equation rendering.
 */
@Composable
fun HighlightedFormulaCard(
    modifier: Modifier = Modifier,
    title: String,
    formula: String,
    explanation: String
) {
    LatexFormulaCard(
        title = title,
        latex = formula,
        explanation = explanation,
        modifier = modifier
    )
}

/**
 * Sticky Note Card for "Common Mistake" or "Teacher Tip".
 * Styled with slight rotation angle, paper shadow, and sticky tape at the top.
 */
@Composable
fun StickyNoteCard(
    modifier: Modifier = Modifier,
    title: String = "Watch Out! Common Student Trap",
    content: String,
    rotationAngle: Float = -1.2f,
    isTip: Boolean = false
) {
    val writingStyle = LocalWritingStyle.current
    val bgColor = if (isTip) Color(0xFFECFDF5) else Color(0xFFFEF9C3)
    val accentColor = if (isTip) Color(0xFF059669) else Color(0xFFDC2626)
    val tapeColor = if (isTip) Color(0x66A7F3D0) else Color(0x77FDE68A)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Sticky note body with gentle angle
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(rotationAngle)
                .shadow(3.dp, RoundedCornerShape(4.dp)),
            shape = RoundedCornerShape(4.dp),
            color = bgColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 14.dp, end = 14.dp, bottom = 14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isTip) "💡 " else "⚠️ ",
                        fontSize = 16.sp
                    )
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontFamily = writingStyle.noteFontFamily
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = content,
                    fontSize = 13.5.sp,
                    color = InkDark,
                    lineHeight = 19.sp,
                    fontFamily = writingStyle.noteFontFamily
                )
            }
        }

        // Semi-transparent Scotch tape at top center
        Box(
            modifier = Modifier
                .width(55.dp)
                .height(14.dp)
                .rotate(rotationAngle * 0.5f)
                .background(tapeColor, RoundedCornerShape(2.dp))
        )
    }
}

/**
 * Hand-Drawn Wobbly Underline / Section Divider.
 */
@Composable
fun SketchedDivider(
    modifier: Modifier = Modifier,
    color: Color = RuledBlue
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
    ) {
        val w = size.width
        val path = Path().apply {
            moveTo(10f, 6f)
            var currentX = 10f
            var wave = 1
            while (currentX < w - 10f) {
                val nextX = (currentX + 35f).coerceAtMost(w - 10f)
                val midX = (currentX + nextX) / 2f
                val midY = 6f + (wave * 2.5f)
                quadraticTo(midX, midY, nextX, 6f)
                currentX = nextX
                wave = -wave
            }
        }
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

/**
 * Pencil Progress Bar.
 * Renders an animated yellow wooden pencil that "draws" its graphite path
 * across the top of the notebook page as the student progresses from Chapter 1 to 25.
 */
@Composable
fun PencilProgressBar(
    currentTopic: Int,
    totalTopics: Int = 25,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val progressFraction = (currentTopic.toFloat() / totalTopics.toFloat()).coerceIn(0.04f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 400),
        label = "pencil_progress"
    )
    val writingStyle = LocalWritingStyle.current

    if (compact) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(PaperCreamDark.copy(alpha = 0.65f))
                .padding(horizontal = 10.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "$currentTopic/$totalTopics",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = InkCharcoal,
                fontFamily = writingStyle.headingFontFamily
            )

            // Sleek inline pencil track
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
            ) {
                val totalW = size.width
                val progressX = (totalW * animatedProgress).coerceAtLeast(18f)

                // 1. Faint guideline
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = Offset(0f, 4f),
                    end = Offset(totalW, 4f),
                    strokeWidth = 1.2f,
                    cap = StrokeCap.Round
                )

                // 2. Graphite sketched line
                drawLine(
                    color = PencilLead,
                    start = Offset(0f, 4f),
                    end = Offset(progressX, 4f),
                    strokeWidth = 1.8f,
                    cap = StrokeCap.Round
                )

                // 3. Mini pencil
                val pencilLen = 16.dp.toPx()
                val tipX = progressX
                val pencilBackX = tipX - pencilLen
                val midY = 4f

                if (pencilBackX > 0) {
                    drawRoundRect(
                        color = Color(0xFFF59E0B),
                        topLeft = Offset(pencilBackX + 3.5.dp.toPx(), midY - 2.dp.toPx()),
                        size = Size(8.dp.toPx(), 4.dp.toPx()),
                        cornerRadius = CornerRadius(0.8.dp.toPx(), 0.8.dp.toPx())
                    )
                    drawRect(
                        color = Color(0xFFCBD5E1),
                        topLeft = Offset(pencilBackX + 1.8.dp.toPx(), midY - 2.dp.toPx()),
                        size = Size(1.7.dp.toPx(), 4.dp.toPx())
                    )
                    drawRoundRect(
                        color = Color(0xFFF472B6),
                        topLeft = Offset(pencilBackX, midY - 2.dp.toPx()),
                        size = Size(1.8.dp.toPx(), 4.dp.toPx()),
                        cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                    )
                    val woodPath = Path().apply {
                        moveTo(pencilBackX + 11.5.dp.toPx(), midY - 2.dp.toPx())
                        lineTo(tipX, midY)
                        lineTo(pencilBackX + 11.5.dp.toPx(), midY + 2.dp.toPx())
                        close()
                    }
                    drawPath(woodPath, color = Color(0xFFFDE68A))
                    drawCircle(color = Color(0xFF1E293B), radius = 1.dp.toPx(), center = Offset(tipX, midY))
                }
            }

            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = InkNavy,
                fontFamily = writingStyle.headingFontFamily
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(PaperCreamDark.copy(alpha = 0.7f))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✏️ Notebook Progress: Page $currentTopic of $totalTopics",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkCharcoal,
                    fontFamily = writingStyle.headingFontFamily
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkNavy,
                    fontFamily = writingStyle.headingFontFamily
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Custom Canvas showing the pencil drawing the line
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                val totalW = size.width
                val progressX = (totalW * animatedProgress).coerceAtLeast(30f)

            // 1. Faint guideline across full width
            drawLine(
                color = Color(0xFFE2E8F0),
                start = Offset(0f, 10f),
                end = Offset(totalW, 10f),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // 2. Graphite sketched line drawn up to pencil point
            val linePath = Path().apply {
                moveTo(0f, 10f)
                var currX = 0f
                while (currX < progressX) {
                    val nx = (currX + 20f).coerceAtMost(progressX)
                    val ny = 10f + ((currX.toInt() % 4) - 1.5f) * 0.6f
                    lineTo(nx, ny)
                    currX = nx
                }
            }
            drawPath(
                path = linePath,
                color = PencilLead,
                style = Stroke(width = 2.5f, cap = StrokeCap.Round)
            )

            // 3. Draw Pencil Body riding at the front of the line
            // Pencil components: Pink eraser -> Silver ferrule -> Yellow body -> Wood cone -> Graphite tip
            val pencilLen = 32.dp.toPx()
            val tipX = progressX
            val pencilBackX = tipX - pencilLen
            val midY = 10f

            if (pencilBackX > 0) {
                // Yellow pencil body
                drawRoundRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(pencilBackX + 8.dp.toPx(), midY - 3.5.dp.toPx()),
                    size = Size(16.dp.toPx(), 7.dp.toPx()),
                    cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                )

                // Metal ferrule
                drawRect(
                    color = Color(0xFFCBD5E1),
                    topLeft = Offset(pencilBackX + 4.dp.toPx(), midY - 3.5.dp.toPx()),
                    size = Size(4.dp.toPx(), 7.dp.toPx())
                )

                // Pink eraser
                drawRoundRect(
                    color = Color(0xFFF472B6),
                    topLeft = Offset(pencilBackX, midY - 3.5.dp.toPx()),
                    size = Size(4.dp.toPx(), 7.dp.toPx()),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )

                // Sharpened wood cone
                val woodPath = Path().apply {
                    moveTo(pencilBackX + 24.dp.toPx(), midY - 3.5.dp.toPx())
                    lineTo(tipX, midY)
                    lineTo(pencilBackX + 24.dp.toPx(), midY + 3.5.dp.toPx())
                    close()
                }
                drawPath(woodPath, color = Color(0xFFFDE68A))

                // Graphite lead tip
                val leadPath = Path().apply {
                    moveTo(tipX - 3.dp.toPx(), midY - 1.2.dp.toPx())
                    lineTo(tipX, midY)
                    lineTo(tipX - 3.dp.toPx(), midY + 1.2.dp.toPx())
                    close()
                }
                drawPath(leadPath, color = Color(0xFF1E293B))
            }
        }
    }
}
}
