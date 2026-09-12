package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiagramType
import kotlin.math.*

/**
 * Renders the hand-drawn sketched diagram corresponding to the topic's DiagramType.
 */
@Composable
fun DiagramRenderer(
    diagramType: DiagramType,
    caption: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFFCFBF6),
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📐 Teacher's Sketchbook",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkNavy,
                    fontFamily = FontFamily.Serif
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (diagramType) {
                DiagramType.INTERACTIVE_TANGENT -> InteractiveTangentWidget()
                DiagramType.INTERACTIVE_RIEMANN_SUM -> InteractiveRiemannWidget()
                DiagramType.FUNCTION_MACHINE -> FunctionMachineDiagram()
                DiagramType.RATE_OF_CHANGE_GRAPH -> RateOfChangeDiagram()
                DiagramType.SLOPE_TRIANGLE -> SlopeTriangleDiagram()
                DiagramType.CURVE_VS_LINE -> CurveVsLineDiagram()
                DiagramType.LIMIT_APPROACH -> LimitApproachDiagram()
                DiagramType.LIMIT_HOLE_GRAPH -> LimitHoleDiagram()
                DiagramType.LIMIT_LAWS_CHART -> LimitLawsDiagram()
                DiagramType.CONTINUITY_TEST -> ContinuityDiagram()
                DiagramType.INSTANTANEOUS_SPEED -> InstantSpeedDiagram()
                DiagramType.FIRST_PRINCIPLES_LIMIT -> FirstPrinciplesDiagram()
                DiagramType.POWER_RULE_VISUAL -> PowerRuleDiagram()
                DiagramType.PRODUCT_CHAIN_RULE -> ChainRuleGearsDiagram()
                DiagramType.TRIG_EXP_SKETCH -> TrigExpDiagram()
                DiagramType.LADDER_PROBLEM -> LadderProblemDiagram()
                DiagramType.AREA_PROBLEM -> AreaProblemDiagram()
                DiagramType.DEFINITE_INTEGRAL_SUM -> DefiniteIntegralDiagram()
                DiagramType.FTC_INVERSE_MACHINE -> FTCInverseDiagram()
                DiagramType.SUBSTITUTION_PUZZLE -> SubstitutionPuzzleDiagram()
                DiagramType.AREA_BETWEEN_CURVES -> AreaBetweenCurvesDiagram()
                DiagramType.SERIES_DOMINOES -> SeriesSquareDiagram()
                DiagramType.TAYLOR_SERIES_FIT -> TaylorFitDiagram()
                DiagramType.PARTIAL_DERIVATIVE_HILL -> PartialHillDiagram()
                DiagramType.DIFF_EQ_SLOPE_FIELD -> SlopeFieldDiagram()
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "✎ $caption",
                fontSize = 12.5.sp,
                color = InkCharcoal,
                fontFamily = FontFamily.Serif,
                lineHeight = 17.sp
            )
        }
    }
}

// --------------------------------------------------------------------------
// 1. INTERACTIVE TANGENT EXPLORER WIDGET
// --------------------------------------------------------------------------
@Composable
fun InteractiveTangentWidget() {
    var selectedFuncIndex by remember { mutableIntStateOf(0) }
    var xPos by remember { mutableFloatStateOf(1.0f) }

    val funcNames = listOf("f(x) = x²", "f(x) = sin(x)", "f(x) = ⅓x³ - x")

    val (fVal, slopeVal) = when (selectedFuncIndex) {
        0 -> Pair(xPos * xPos, 2f * xPos)
        1 -> Pair(sin(xPos), cos(xPos))
        else -> Pair((xPos * xPos * xPos) / 3f - xPos, (xPos * xPos) - 1f)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Function Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            funcNames.forEachIndexed { index, name ->
                FilterChip(
                    selected = selectedFuncIndex == index,
                    onClick = { selectedFuncIndex = index },
                    label = { Text(name, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HighlighterYellowSolid,
                        selectedLabelColor = InkNavy
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Sketched Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFFFFDF8), RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
        ) {
            val w = size.width
            val h = size.height
            val originX = w / 2f
            val originY = h / 2f
            val scaleX = w / 6f
            val scaleY = h / 4.5f

            // Sketched Grid & Axes
            drawSketchedAxes(originX, originY, w, h)

            // Draw Curve
            val curvePath = Path()
            var started = false
            var step = -2.8f
            while (step <= 2.8f) {
                val yMath = when (selectedFuncIndex) {
                    0 -> step * step
                    1 -> sin(step)
                    else -> (step * step * step) / 3f - step
                }
                val px = originX + step * scaleX
                val py = originY - yMath * scaleY
                if (!started) {
                    curvePath.moveTo(px, py)
                    started = true
                } else {
                    curvePath.lineTo(px, py)
                }
                step += 0.08f
            }
            drawPath(
                path = curvePath,
                color = InkNavy,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // Current Touchpoint P
            val ptX = originX + xPos * scaleX
            val ptY = originY - fVal * scaleY

            // Tangent Line: y - y0 = m (x - x0)
            val tangentLen = 1.6f
            val x1 = xPos - tangentLen
            val y1 = fVal - slopeVal * tangentLen
            val x2 = xPos + tangentLen
            val y2 = fVal + slopeVal * tangentLen

            val tX1 = originX + x1 * scaleX
            val tY1 = originY - y1 * scaleY
            val tX2 = originX + x2 * scaleX
            val tY2 = originY - y2 * scaleY

            // Sketched red tangent line
            drawLine(
                color = CorrectionRed,
                start = Offset(tX1, tY1),
                end = Offset(tX2, tY2),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Highlight touchpoint
            drawCircle(color = Color(0xFFF59E0B), radius = 5.dp.toPx(), center = Offset(ptX, ptY))
            drawCircle(
                color = InkNavy,
                radius = 5.dp.toPx(),
                center = Offset(ptX, ptY),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Slider control for x
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Point x: ${"%.2f".format(xPos)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = InkNavy,
                modifier = Modifier.width(90.dp)
            )
            Slider(
                value = xPos,
                onValueChange = { xPos = it },
                valueRange = -2.2f..2.2f,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Readout Badge
        Row(
            modifier = Modifier
                .background(HighlighterYellow, RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("f(x) = ${"%.2f".format(fVal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkNavy)
            Text("Tangent Slope f'(x) = ${"%.2f".format(slopeVal)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CorrectionRed)
        }
    }
}

// --------------------------------------------------------------------------
// 2. INTERACTIVE RIEMANN SUM WIDGET
// --------------------------------------------------------------------------
@Composable
fun InteractiveRiemannWidget() {
    var rectCount by remember { mutableFloatStateOf(4f) }
    var sumMethod by remember { mutableIntStateOf(0) } // 0: Left, 1: Right, 2: Midpoint

    val n = rectCount.toInt()
    val a = 0f
    val b = 2.5f
    val dx = (b - a) / n

    // Target function: f(x) = 0.3 * x^2 + 0.4
    // Exact integral on [0, 2.5] = 0.3 * (2.5^3 / 3) + 0.4 * 2.5 = 0.1 * 15.625 + 1.0 = 2.5625
    val exactArea = 2.5625f

    var approxArea = 0f
    for (i in 0 until n) {
        val xEval = when (sumMethod) {
            0 -> a + i * dx // left
            1 -> a + (i + 1) * dx // right
            else -> a + (i + 0.5f) * dx // midpoint
        }
        val h = 0.3f * xEval * xEval + 0.4f
        approxArea += h * dx
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mode Selector: Left, Midpoint, Right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Left Sum", "Midpoint Sum", "Right Sum").forEachIndexed { idx, label ->
                FilterChip(
                    selected = sumMethod == idx,
                    onClick = { sumMethod = idx },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = HighlighterYellowSolid,
                        selectedLabelColor = InkNavy
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Sketched Riemann Canvas
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color(0xFFFFFDF8), RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
        ) {
            val w = size.width
            val h = size.height
            val originX = 35.dp.toPx()
            val originY = h - 25.dp.toPx()
            val scaleX = (w - 60.dp.toPx()) / 3f
            val scaleY = (h - 50.dp.toPx()) / 2.6f

            // Axes
            drawLine(PencilLead, Offset(originX - 10f, originY), Offset(w - 15f, originY), 1.8f)
            drawLine(PencilLead, Offset(originX, originY + 10f), Offset(originX, 15f), 1.8f)

            // Draw Riemann Rectangles
            for (i in 0 until n) {
                val xLeft = a + i * dx
                val xEval = when (sumMethod) {
                    0 -> xLeft
                    1 -> xLeft + dx
                    else -> xLeft + dx / 2f
                }
                val rectHeightMath = 0.3f * xEval * xEval + 0.4f

                val rx = originX + xLeft * scaleX
                val rw = dx * scaleX
                val rh = rectHeightMath * scaleY
                val ry = originY - rh

                // Fill with translucent yellow highlighter
                drawRect(
                    color = Color(0x66FEF08A),
                    topLeft = Offset(rx, ry),
                    size = Size(rw, rh)
                )
                // Sketched rectangle border
                drawRect(
                    color = InkNavy.copy(alpha = 0.75f),
                    topLeft = Offset(rx, ry),
                    size = Size(rw, rh),
                    style = Stroke(width = 1.2f)
                )
                // Subtle pencil hatching inside rectangle
                var hatchY = ry + 8f
                while (hatchY < ry + rh - 4f) {
                    drawLine(
                        color = Color(0x333B82F6),
                        start = Offset(rx + 2f, hatchY),
                        end = Offset(rx + rw - 2f, hatchY),
                        strokeWidth = 0.8f
                    )
                    hatchY += 12f
                }
            }

            // Draw Smooth Function Curve on top
            val curvePath = Path()
            var first = true
            var step = 0f
            while (step <= 2.8f) {
                val yMath = 0.3f * step * step + 0.4f
                val px = originX + step * scaleX
                val py = originY - yMath * scaleY
                if (first) {
                    curvePath.moveTo(px, py)
                    first = false
                } else {
                    curvePath.lineTo(px, py)
                }
                step += 0.05f
            }
            drawPath(
                path = curvePath,
                color = InkNavy,
                style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Slider for n
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rectangles n = $n",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = InkNavy,
                modifier = Modifier.width(115.dp)
            )
            Slider(
                value = rectCount,
                onValueChange = { rectCount = it },
                valueRange = 2f..30f,
                steps = 28,
                modifier = Modifier.weight(1f)
            )
        }

        // Area readout comparison
        Row(
            modifier = Modifier
                .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Riemann Sum ≈ ${"%.3f".format(approxArea)}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = InkNavy)
            Text("Exact ∫ = ${"%.3f".format(exactArea)}", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = CorrectionRed)
            val error = abs(approxArea - exactArea)
            Text("Error: ${"%.3f".format(error)}", fontSize = 11.5.sp, color = InkCharcoal)
        }
    }
}

// --------------------------------------------------------------------------
// 3. STATIC HAND-SKETCHED DIAGRAMS FOR ALL TOPICS
// --------------------------------------------------------------------------

@Composable
fun FunctionMachineDiagram() {
    StaticDiagramCanvas { w, h ->
        // Input funnel on top, processor box, output chute
        val boxW = 140.dp.toPx()
        val boxH = 70.dp.toPx()
        val boxX = (w - boxW) / 2f
        val boxY = (h - boxH) / 2f

        // Sketched Main Box
        drawSketchedRect(boxX, boxY, boxW, boxH, HighlighterYellow, InkNavy)

        // Funnel at top
        val funnelPath = Path().apply {
            moveTo(boxX + 40f, boxY)
            lineTo(boxX + 25f, boxY - 30f)
            lineTo(boxX + boxW - 25f, boxY - 30f)
            lineTo(boxX + boxW - 40f, boxY)
        }
        drawPath(funnelPath, InkNavy, style = Stroke(width = 2f, cap = StrokeCap.Round))

        // Input Arrow: x = 3
        drawSketchedArrow(Offset(w / 2f, boxY - 45f), Offset(w / 2f, boxY - 15f), CorrectionRed)

        // Output Chute at bottom
        val chutePath = Path().apply {
            moveTo(boxX + 50f, boxY + boxH)
            lineTo(boxX + 50f, boxY + boxH + 25f)
            moveTo(boxX + boxW - 50f, boxY + boxH)
            lineTo(boxX + boxW - 50f, boxY + boxH + 25f)
        }
        drawPath(chutePath, InkNavy, style = Stroke(width = 2f, cap = StrokeCap.Round))

        // Output Arrow: y = 7
        drawSketchedArrow(Offset(w / 2f, boxY + boxH + 5f), Offset(w / 2f, boxY + boxH + 35f), CorrectionRed)
    }
}

@Composable
fun RateOfChangeDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h - 25.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Curved wiggly path of a car journey
        val path = Path().apply {
            moveTo(ox, oy)
            cubicTo(ox + 40f, oy - 20f, ox + 60f, oy - 70f, ox + 110f, oy - 50f)
            cubicTo(ox + 150f, oy - 30f, ox + 180f, oy - 120f, ox + 220f, oy - 110f)
        }
        drawPath(path, InkNavy, style = Stroke(width = 2.5f, cap = StrokeCap.Round))

        // Straight chord showing average speed
        drawLine(
            color = CorrectionRed,
            start = Offset(ox, oy),
            end = Offset(ox + 220f, oy - 110f),
            strokeWidth = 1.8f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
        )
    }
}

@Composable
fun SlopeTriangleDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 35.dp.toPx()
        val oy = h - 25.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        val p1 = Offset(ox + 30f, oy - 30f)
        val p2 = Offset(ox + 190f, oy - 110f)
        val pCorner = Offset(p2.x, p1.y)

        // The line
        drawLine(InkNavy, Offset(p1.x - 20f, p1.y + 10f), Offset(p2.x + 20f, p2.y - 10f), 2.5f)

        // Rise / Run Triangle in Red
        val triPath = Path().apply {
            moveTo(p1.x, p1.y)
            lineTo(pCorner.x, pCorner.y)
            lineTo(p2.x, p2.y)
        }
        drawPath(triPath, CorrectionRed, style = Stroke(width = 1.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f))))

        // Right angle marker
        drawRect(Color.Transparent, topLeft = Offset(pCorner.x - 12f, pCorner.y - 12f), size = Size(12f, 12f), style = Stroke(1.2f))
    }
}

@Composable
fun CurveVsLineDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 35.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Curve f(x) = x^2
        val curve = Path().apply {
            moveTo(ox + 10f, oy - 5f)
            quadraticTo(ox + 110f, oy - 20f, ox + 220f, oy - 120f)
        }
        drawPath(curve, InkNavy, style = Stroke(width = 2.5f))

        // Secant line cutting through
        val pA = Offset(ox + 50f, oy - 12f)
        val pB = Offset(ox + 180f, oy - 80f)
        drawLine(CorrectionRed, Offset(pA.x - 25f, pA.y + 15f), Offset(pB.x + 25f, pB.y - 15f), 1.8f)

        drawCircle(Color(0xFFF59E0B), 4.dp.toPx(), pA)
        drawCircle(Color(0xFFF59E0B), 4.dp.toPx(), pB)
    }
}

@Composable
fun LimitApproachDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        val hole = Offset(w / 2f, h / 2f)

        // Left approach curve
        val leftCurve = Path().apply {
            moveTo(ox + 20f, oy - 20f)
            quadraticTo(hole.x - 40f, hole.y + 30f, hole.x - 6f, hole.y)
        }
        drawPath(leftCurve, InkNavy, style = Stroke(width = 2.5f))

        // Right approach curve
        val rightCurve = Path().apply {
            moveTo(w - 25f, 25f)
            quadraticTo(hole.x + 40f, hole.y - 30f, hole.x + 6f, hole.y)
        }
        drawPath(rightCurve, InkNavy, style = Stroke(width = 2.5f))

        // Open hole circle
        drawCircle(Color.White, 6.dp.toPx(), hole)
        drawCircle(CorrectionRed, 6.dp.toPx(), hole, style = Stroke(2.5f))

        // Approaching arrows
        drawSketchedArrow(Offset(hole.x - 55f, hole.y + 25f), Offset(hole.x - 15f, hole.y + 8f), Color(0xFF059669))
        drawSketchedArrow(Offset(hole.x + 55f, hole.y - 25f), Offset(hole.x + 15f, hole.y - 8f), Color(0xFF059669))
    }
}

@Composable
fun LimitHoleDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Left line to hole
        val holePos = Offset(w * 0.45f, h * 0.45f)
        drawLine(InkNavy, Offset(ox + 20f, oy - 20f), Offset(holePos.x - 5f, holePos.y), 2.5f)

        // Jump to higher line from right
        val jumpStart = Offset(holePos.x + 5f, h * 0.25f)
        drawLine(InkNavy, Offset(jumpStart.x, jumpStart.y), Offset(w - 20f, h * 0.15f), 2.5f)

        // Open circle at bottom, solid at top
        drawCircle(Color.White, 5.dp.toPx(), holePos)
        drawCircle(CorrectionRed, 5.dp.toPx(), holePos, style = Stroke(2f))

        drawCircle(CorrectionRed, 5.dp.toPx(), jumpStart)

        // Dashed gap
        drawLine(
            color = PencilLead,
            start = holePos,
            end = jumpStart,
            strokeWidth = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
        )
    }
}

@Composable
fun LimitLawsDiagram() {
    StaticDiagramCanvas { w, h ->
        // Sketched mathematical balance scale or toolbox
        val midX = w / 2f
        val midY = h / 2f

        // Central balance beam
        drawLine(InkNavy, Offset(midX - 90f, midY), Offset(midX + 90f, midY), 3f)
        // Fulcrum
        val fulcrum = Path().apply {
            moveTo(midX, midY)
            lineTo(midX - 18f, midY + 32f)
            lineTo(midX + 18f, midY + 32f)
            close()
        }
        drawPath(fulcrum, HighlighterYellow, style = Stroke(0f))
        drawPath(fulcrum, InkNavy, style = Stroke(2f))

        // Left pan: lim(f + g)
        drawLine(PencilLead, Offset(midX - 70f, midY), Offset(midX - 70f, midY + 25f), 1.5f)
        drawRect(Color(0xFFFEF3C7), topLeft = Offset(midX - 100f, midY + 25f), size = Size(60f, 25f))
        drawRect(InkNavy, topLeft = Offset(midX - 100f, midY + 25f), size = Size(60f, 25f), style = Stroke(1.5f))

        // Right pan: lim f + lim g
        drawLine(PencilLead, Offset(midX + 70f, midY), Offset(midX + 70f, midY + 25f), 1.5f)
        drawRect(Color(0xFFECFDF5), topLeft = Offset(midX + 40f, midY + 25f), size = Size(60f, 25f))
        drawRect(InkNavy, topLeft = Offset(midX + 40f, midY + 25f), size = Size(60f, 25f), style = Stroke(1.5f))
    }
}

@Composable
fun ContinuityDiagram() {
    StaticDiagramCanvas { w, h ->
        // Side-by-side comparison: Continuous (smooth unbroken) vs Discontinuous (hole / jump)
        val midX = w / 2f
        drawLine(RuledBlue, Offset(midX, 15f), Offset(midX, h - 15f), 1.5f)

        // Left: Smooth wave
        val smoothPath = Path().apply {
            moveTo(25f, h * 0.7f)
            cubicTo(midX * 0.35f, h * 0.2f, midX * 0.65f, h * 0.8f, midX - 25f, h * 0.3f)
        }
        drawPath(smoothPath, Color(0xFF059669), style = Stroke(2.5f, cap = StrokeCap.Round))

        // Right: Broken curve with hole
        val brokenLeft = Path().apply {
            moveTo(midX + 25f, h * 0.7f)
            quadraticTo(midX + 60f, h * 0.35f, midX + 85f, h * 0.5f)
        }
        drawPath(brokenLeft, CorrectionRed, style = Stroke(2.5f, cap = StrokeCap.Round))
        drawCircle(Color.White, 5.dp.toPx(), Offset(midX + 90f, h * 0.5f))
        drawCircle(CorrectionRed, 5.dp.toPx(), Offset(midX + 90f, h * 0.5f), style = Stroke(2f))

        val brokenRight = Path().apply {
            moveTo(midX + 95f, h * 0.3f)
            lineTo(w - 25f, h * 0.2f)
        }
        drawPath(brokenRight, CorrectionRed, style = Stroke(2.5f, cap = StrokeCap.Round))
    }
}

@Composable
fun InstantSpeedDiagram() {
    StaticDiagramCanvas { w, h ->
        // Speedometer dial with needle pointing right now
        val cx = w / 2f
        val cy = h * 0.7f
        val radius = 65.dp.toPx()

        // Speedometer arc
        drawArc(
            color = InkNavy,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(cx - radius, cy - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Tick marks
        for (i in 0..6) {
            val angleRad = Math.toRadians((180 + i * 30).toDouble())
            val inner = Offset((cx + (radius - 12f) * cos(angleRad)).toFloat(), (cy + (radius - 12f) * sin(angleRad)).toFloat())
            val outer = Offset((cx + radius * cos(angleRad)).toFloat(), (cy + radius * sin(angleRad)).toFloat())
            drawLine(InkNavy, inner, outer, 1.8f)
        }

        // Needle pointing to 60 mph in bright red
        val needleAngle = Math.toRadians(240.0)
        val needleEnd = Offset((cx + (radius - 8f) * cos(needleAngle)).toFloat(), (cy + (radius - 8f) * sin(needleAngle)).toFloat())
        drawLine(
            color = CorrectionRed,
            start = Offset(cx, cy),
            end = needleEnd,
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )
        drawCircle(InkDark, 5.dp.toPx(), Offset(cx, cy))
    }
}

@Composable
fun FirstPrinciplesDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Curve
        val pP = Offset(ox + 60f, oy - 35f)
        val pQ = Offset(ox + 180f, oy - 110f)

        val curve = Path().apply {
            moveTo(ox + 20f, oy - 10f)
            quadraticTo(ox + 120f, oy - 30f, ox + 220f, oy - 125f)
        }
        drawPath(curve, InkNavy, style = Stroke(2.5f))

        // Secant chord
        drawLine(CorrectionRed, pP, pQ, 1.8f)

        // Run h and Rise [f(x+h) - f(x)]
        val corner = Offset(pQ.x, pP.y)
        drawLine(
            color = PencilLead,
            start = pP,
            end = corner,
            strokeWidth = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
        )
        drawLine(
            color = PencilLead,
            start = corner,
            end = pQ,
            strokeWidth = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
        )

        // Sliding arrow h -> 0
        drawSketchedArrow(Offset(pQ.x - 20f, pQ.y + 15f), Offset(pP.x + 35f, pP.y - 10f), Color(0xFF059669))
    }
}

@Composable
fun PowerRuleDiagram() {
    StaticDiagramCanvas { w, h ->
        // Geometric representation: x^3 is a cube; peeling its faces gives 3x^2!
        val cx = w / 2f
        val cy = h / 2f

        // Draw 3 unfolded face squares
        val side = 40.dp.toPx()
        val gap = 12.dp.toPx()

        val startX = cx - (side * 1.5f + gap)
        for (i in 0..2) {
            val sx = startX + i * (side + gap)
            val sy = cy - side / 2f
            drawSketchedRect(sx, sy, side, side, HighlighterYellow, InkNavy)
        }
    }
}

@Composable
fun ChainRuleGearsDiagram() {
    StaticDiagramCanvas { w, h ->
        // Two interlocking sketched gears
        val c1 = Offset(w * 0.38f, h * 0.5f)
        val r1 = 36.dp.toPx()
        val c2 = Offset(w * 0.65f, h * 0.5f)
        val r2 = 25.dp.toPx()

        drawCircle(Color(0xFFFEF3C7), r1, c1)
        drawCircle(InkNavy, r1, c1, style = Stroke(2f))

        drawCircle(Color(0xFFECFDF5), r2, c2)
        drawCircle(CorrectionRed, r2, c2, style = Stroke(2f))

        // Gear teeth accents
        for (i in 0 until 8) {
            val a = Math.toRadians((i * 45).toDouble())
            val p1 = Offset((c1.x + (r1 - 4f) * cos(a)).toFloat(), (c1.y + (r1 - 4f) * sin(a)).toFloat())
            val p2 = Offset((c1.x + (r1 + 6f) * cos(a)).toFloat(), (c1.y + (r1 + 6f) * sin(a)).toFloat())
            drawLine(InkNavy, p1, p2, 2.5f)
        }

        for (i in 0 until 6) {
            val a = Math.toRadians((i * 60 + 15).toDouble())
            val p1 = Offset((c2.x + (r2 - 4f) * cos(a)).toFloat(), (c2.y + (r2 - 4f) * sin(a)).toFloat())
            val p2 = Offset((c2.x + (r2 + 5f) * cos(a)).toFloat(), (c2.y + (r2 + 5f) * sin(a)).toFloat())
            drawLine(CorrectionRed, p1, p2, 2f)
        }
    }
}

@Composable
fun TrigExpDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 25.dp.toPx()
        val oy = h / 2f
        drawSketchedAxes(ox, oy, w, h)

        // Sine wave in Blue
        val sinPath = Path()
        val cosPath = Path()
        val scaleX = (w - 45.dp.toPx()) / (2f * Math.PI.toFloat())
        val amp = 40.dp.toPx()

        var first = true
        var t = 0f
        while (t <= 2f * Math.PI.toFloat()) {
            val px = ox + t * scaleX
            val pySin = oy - sin(t) * amp
            val pyCos = oy - cos(t) * amp
            if (first) {
                sinPath.moveTo(px, pySin)
                cosPath.moveTo(px, pyCos)
                first = false
            } else {
                sinPath.lineTo(px, pySin)
                cosPath.lineTo(px, pyCos)
            }
            t += 0.1f
        }
        drawPath(sinPath, InkNavy, style = Stroke(2.5f))
        drawPath(cosPath, CorrectionRed, style = Stroke(1.8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f))))
    }
}

@Composable
fun LadderProblemDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = w * 0.35f
        val oy = h - 25.dp.toPx()

        // Wall and floor
        drawLine(PencilLead, Offset(ox, 20f), Offset(ox, oy), 3f)
        drawLine(PencilLead, Offset(ox, oy), Offset(w - 20f, oy), 3f)

        // Ladder leaning from (ox, oy - 90f) to (ox + 120f, oy)
        val top = Offset(ox, oy - 90f)
        val bottom = Offset(ox + 120f, oy)
        drawLine(
            color = Color(0xFF92400E),
            start = top,
            end = bottom,
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Sliding arrows
        drawSketchedArrow(bottom, Offset(bottom.x + 35f, bottom.y), CorrectionRed) // dx/dt
        drawSketchedArrow(top, Offset(top.x, top.y + 35f), CorrectionRed) // dy/dt
    }
}

@Composable
fun AreaProblemDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Shaded curved area
        val areaPath = Path().apply {
            moveTo(ox + 30f, oy)
            lineTo(ox + 30f, oy - 40f)
            quadraticTo(ox + 100f, oy - 110f, ox + 180f, oy - 50f)
            lineTo(ox + 180f, oy)
            close()
        }
        drawPath(areaPath, HighlighterYellow)

        // Hatching lines
        var hx = ox + 40f
        while (hx < ox + 175f) {
            drawLine(Color(0x441E3A8A), Offset(hx, oy), Offset(hx, oy - 65f), 1f)
            hx += 14f
        }

        // Boundary curve
        val boundary = Path().apply {
            moveTo(ox + 20f, oy - 25f)
            quadraticTo(ox + 100f, oy - 110f, ox + 200f, oy - 40f)
        }
        drawPath(boundary, InkNavy, style = Stroke(2.5f))
    }
}

@Composable
fun DefiniteIntegralDiagram() {
    StaticDiagramCanvas { w, h ->
        // Beautiful large elongated ∫ with a and b limits
        val cx = w * 0.35f
        val cy = h * 0.5f

        // Drawn stylized integral symbol
        val sPath = Path().apply {
            moveTo(cx + 15f, cy - 45f)
            cubicTo(cx - 5f, cy - 50f, cx - 12f, cy - 35f, cx, cy)
            cubicTo(cx + 12f, cy + 35f, cx + 5f, cy + 50f, cx - 15f, cy + 45f)
        }
        drawPath(sPath, InkNavy, style = Stroke(3.5.dp.toPx(), cap = StrokeCap.Round))

        // Shaded region preview next to it
        val rx = w * 0.65f
        val ry = h * 0.65f
        val shape = Path().apply {
            moveTo(rx - 30f, ry)
            lineTo(rx - 30f, ry - 35f)
            quadraticTo(rx, ry - 65f, rx + 30f, ry - 40f)
            lineTo(rx + 30f, ry)
            close()
        }
        drawPath(shape, HighlighterGreen)
        drawPath(shape, Color(0xFF059669), style = Stroke(1.8f))
    }
}

@Composable
fun FTCInverseDiagram() {
    StaticDiagramCanvas { w, h ->
        val midY = h / 2f
        val b1 = Offset(w * 0.25f, midY)
        val b2 = Offset(w * 0.75f, midY)

        // Box 1: Rates / Slopes
        drawSketchedRect(b1.x - 45f, b1.y - 25f, 90f, 50f, Color(0xFFFEF3C7), InkNavy)

        // Box 2: Accumulation / Area
        drawSketchedRect(b2.x - 45f, b2.y - 25f, 90f, 50f, Color(0xFFECFDF5), InkNavy)

        // Top Arrow: Integrate ->
        drawSketchedArrow(Offset(b1.x + 50f, midY - 12f), Offset(b2.x - 50f, midY - 12f), Color(0xFF059669))

        // Bottom Arrow: Differentiate <-
        drawSketchedArrow(Offset(b2.x - 50f, midY + 12f), Offset(b1.x + 50f, midY + 12f), CorrectionRed)
    }
}

@Composable
fun SubstitutionPuzzleDiagram() {
    StaticDiagramCanvas { w, h ->
        // Puzzle pieces connecting u and du
        val cx = w / 2f
        val cy = h / 2f
        val pw = 65.dp.toPx()
        val ph = 40.dp.toPx()

        drawSketchedRect(cx - pw - 10f, cy - ph / 2f, pw, ph, HighlighterYellow, InkNavy)
        drawSketchedRect(cx + 10f, cy - ph / 2f, pw, ph, Color(0xFFE0E7FF), InkNavy)

        // Plus and equals connection
        drawLine(CorrectionRed, Offset(cx - 5f, cy), Offset(cx + 5f, cy), 2f)
    }
}

@Composable
fun AreaBetweenCurvesDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 35.dp.toPx()
        val oy = h - 20.dp.toPx()
        drawSketchedAxes(ox, oy, w, h)

        // Top line y = x
        val line = Path().apply {
            moveTo(ox + 20f, oy - 20f)
            lineTo(ox + 180f, oy - 110f)
        }
        drawPath(line, InkNavy, style = Stroke(2f))

        // Bottom curve y = x^2
        val parabola = Path().apply {
            moveTo(ox + 20f, oy - 20f)
            quadraticTo(ox + 100f, oy - 35f, ox + 180f, oy - 110f)
        }
        drawPath(parabola, CorrectionRed, style = Stroke(2f))

        // Shaded region trapped between
        val trapped = Path().apply {
            moveTo(ox + 20f, oy - 20f)
            lineTo(ox + 180f, oy - 110f)
            quadraticTo(ox + 100f, oy - 35f, ox + 20f, oy - 20f)
            close()
        }
        drawPath(trapped, HighlighterYellow)
    }
}

@Composable
fun SeriesSquareDiagram() {
    StaticDiagramCanvas { w, h ->
        // Unit square dissected: 1/2, 1/4, 1/8, 1/16...
        val s = 90.dp.toPx()
        val ox = (w - s) / 2f
        val oy = (h - s) / 2f

        // Outer square
        drawRect(Color.White, Offset(ox, oy), Size(s, s))
        drawRect(InkNavy, Offset(ox, oy), Size(s, s), style = Stroke(2f))

        // Left half = 1/2
        drawRect(HighlighterYellow, Offset(ox, oy), Size(s / 2f, s))
        drawLine(InkNavy, Offset(ox + s / 2f, oy), Offset(ox + s / 2f, oy + s), 1.5f)

        // Top-right quadrant = 1/4
        drawRect(Color(0xFFE0E7FF), Offset(ox + s / 2f, oy), Size(s / 2f, s / 2f))
        drawLine(InkNavy, Offset(ox + s / 2f, oy + s / 2f), Offset(ox + s, oy + s / 2f), 1.5f)

        // Bottom-right slice = 1/8
        drawRect(Color(0xFFFECDD3), Offset(ox + s / 2f, oy + s / 2f), Size(s / 4f, s / 2f))
        drawLine(InkNavy, Offset(ox + s * 0.75f, oy + s / 2f), Offset(ox + s * 0.75f, oy + s), 1.5f)
    }
}

@Composable
fun TaylorFitDiagram() {
    StaticDiagramCanvas { w, h ->
        val ox = 30.dp.toPx()
        val oy = h / 2f
        drawSketchedAxes(ox, oy, w, h)

        // Target Sine curve in Navy
        val sinPath = Path()
        val scaleX = (w - 50.dp.toPx()) / 4f
        var t = -1.8f
        var first = true
        while (t <= 1.8f) {
            val px = w / 2f + t * scaleX
            val py = oy - sin(t) * 45f
            if (first) { sinPath.moveTo(px, py); first = false } else { sinPath.lineTo(px, py) }
            t += 0.08f
        }
        drawPath(sinPath, InkNavy, style = Stroke(3f))

        // Tangent line (1st degree) in gray dashed
        drawLine(
            color = PencilLead,
            start = Offset(w / 2f - 60f, oy + 60f),
            end = Offset(w / 2f + 60f, oy - 60f),
            strokeWidth = 1.5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 4f))
        )

        // Cubic approximation: x - x^3/6 in red
        val cubicPath = Path()
        t = -1.8f
        first = true
        while (t <= 1.8f) {
            val px = w / 2f + t * scaleX
            val yCubic = t - (t * t * t) / 6f
            val py = oy - yCubic * 45f
            if (first) { cubicPath.moveTo(px, py); first = false } else { cubicPath.lineTo(px, py) }
            t += 0.08f
        }
        drawPath(cubicPath, CorrectionRed, style = Stroke(1.8f))
    }
}

@Composable
fun PartialHillDiagram() {
    StaticDiagramCanvas { w, h ->
        val cx = w / 2f
        val cy = h / 2f

        // Contour ellipses representing mountain altitude
        drawOval(Color(0xFFFEF3C7), Offset(cx - 70f, cy - 40f), Size(140f, 80f))
        drawOval(InkNavy, Offset(cx - 70f, cy - 40f), Size(140f, 80f), style = Stroke(1.5f))

        drawOval(Color(0xFFFDE68A), Offset(cx - 45f, cy - 25f), Size(90f, 50f))
        drawOval(InkNavy, Offset(cx - 45f, cy - 25f), Size(90f, 50f), style = Stroke(1.5f))

        drawOval(Color(0xFFF59E0B), Offset(cx - 20f, cy - 10f), Size(40f, 20f))

        // East-West slice line (∂f/∂x) with arrow
        drawLine(CorrectionRed, Offset(cx - 85f, cy), Offset(cx + 85f, cy), 2f)
        drawSketchedArrow(Offset(cx + 60f, cy), Offset(cx + 88f, cy), CorrectionRed)
    }
}

@Composable
fun SlopeFieldDiagram() {
    StaticDiagramCanvas { w, h ->
        // Grid of tiny tangent slope dashes representing dy/dx = y
        val rows = 5
        val cols = 7
        val stepX = (w - 40.dp.toPx()) / cols
        val stepY = (h - 30.dp.toPx()) / rows

        for (r in 0..rows) {
            val yVal = (r - rows / 2f)
            val slope = yVal * 0.4f
            val angle = atan(slope)
            val len = 8.dp.toPx()

            for (c in 0..cols) {
                val px = 20.dp.toPx() + c * stepX
                val py = 15.dp.toPx() + r * stepY

                val dx = len * cos(angle)
                val dy = -len * sin(angle)

                drawLine(
                    color = if (abs(yVal) < 0.1f) CorrectionRed else InkNavy.copy(alpha = 0.6f),
                    start = Offset(px - dx, py - dy),
                    end = Offset(px + dx, py + dy),
                    strokeWidth = 1.5f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

// --------------------------------------------------------------------------
// HELPER CANVAS UTILITIES
// --------------------------------------------------------------------------

@Composable
fun StaticDiagramCanvas(
    onDraw: DrawScope.(Float, Float) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color(0xFFFFFDF8), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
    ) {
        onDraw(size.width, size.height)
    }
}

fun DrawScope.drawSketchedAxes(originX: Float, originY: Float, w: Float, h: Float) {
    // X-Axis
    drawLine(
        color = PencilLead,
        start = Offset(15f, originY),
        end = Offset(w - 15f, originY),
        strokeWidth = 1.8f,
        cap = StrokeCap.Round
    )
    // X arrow
    drawLine(PencilLead, Offset(w - 22f, originY - 4f), Offset(w - 15f, originY), 1.8f)
    drawLine(PencilLead, Offset(w - 22f, originY + 4f), Offset(w - 15f, originY), 1.8f)

    // Y-Axis
    drawLine(
        color = PencilLead,
        start = Offset(originX, h - 15f),
        end = Offset(originX, 15f),
        strokeWidth = 1.8f,
        cap = StrokeCap.Round
    )
    // Y arrow
    drawLine(PencilLead, Offset(originX - 4f, 22f), Offset(originX, 15f), 1.8f)
    drawLine(PencilLead, Offset(originX + 4f, 22f), Offset(originX, 15f), 1.8f)
}

fun DrawScope.drawSketchedArrow(start: Offset, end: Offset, color: Color) {
    drawLine(color, start, end, 2f, StrokeCap.Round)
    val angle = atan2(end.y - start.y, end.x - start.x)
    val arrowLen = 10f
    val a1 = angle + Math.toRadians(150.0).toFloat()
    val a2 = angle - Math.toRadians(150.0).toFloat()
    drawLine(color, end, Offset(end.x + arrowLen * cos(a1), end.y + arrowLen * sin(a1)), 2f, StrokeCap.Round)
    drawLine(color, end, Offset(end.x + arrowLen * cos(a2), end.y + arrowLen * sin(a2)), 2f, StrokeCap.Round)
}

fun DrawScope.drawSketchedRect(x: Float, y: Float, w: Float, h: Float, fillColor: Color, strokeColor: Color) {
    drawRect(fillColor, Offset(x, y), Size(w, h))
    drawRect(strokeColor, Offset(x, y), Size(w, h), style = Stroke(1.8f))
}
