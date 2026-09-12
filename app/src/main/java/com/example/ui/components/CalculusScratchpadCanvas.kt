package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.LocalWritingStyle
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.hypot

/**
 * Supported Canvas Drawing Tools
 */
enum class CanvasTool(val label: String) {
    PEN("Pen"),
    HIGHLIGHTER("Highlighter"),
    LINE("Straight Line"),
    ERASER("Eraser")
}

/**
 * Canvas Background Grid Modes
 */
enum class CanvasBackground(val label: String, val icon: String) {
    CARTESIAN("Graph (XY)", "⊞"),
    LINED("Lined Paper", "≡"),
    DOTS("Dot Grid", "⁖"),
    BLANK("Blank", "□")
}

/**
 * Digital Canvas Stroke Data Representation
 */
data class CanvasPoint(val x: Float, val y: Float) {
    fun toOffset(): Offset = Offset(x, y)
}

data class CanvasStroke(
    val points: List<CanvasPoint>,
    val colorInt: Int,
    val strokeWidth: Float,
    val isHighlighter: Boolean = false,
    val isStraightLine: Boolean = false,
    val isDashed: Boolean = false
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("c", colorInt)
        json.put("w", strokeWidth.toDouble())
        json.put("h", isHighlighter)
        json.put("s", isStraightLine)
        json.put("d", isDashed)
        val ptsArray = JSONArray()
        points.forEach { pt ->
            ptsArray.put(pt.x.toDouble())
            ptsArray.put(pt.y.toDouble())
        }
        json.put("p", ptsArray)
        return json
    }

    companion object {
        fun fromJson(json: JSONObject): CanvasStroke? {
            return try {
                val color = json.optInt("c", Color.Black.toArgb())
                val width = json.optDouble("w", 4.0).toFloat()
                val highlighter = json.optBoolean("h", false)
                val straight = json.optBoolean("s", false)
                val dashed = json.optBoolean("d", false)
                val ptsArray = json.optJSONArray("p") ?: return null
                val pts = mutableListOf<CanvasPoint>()
                for (i in 0 until ptsArray.length() step 2) {
                    if (i + 1 < ptsArray.length()) {
                        pts.add(CanvasPoint(ptsArray.getDouble(i).toFloat(), ptsArray.getDouble(i + 1).toFloat()))
                    }
                }
                if (pts.isEmpty()) null else CanvasStroke(pts, color, width, highlighter, straight, dashed)
            } catch (e: Exception) {
                null
            }
        }
    }
}

object CanvasSerializationHelper {
    fun serializeStrokes(strokes: List<CanvasStroke>): String {
        val arr = JSONArray()
        strokes.forEach { stroke ->
            arr.put(stroke.toJson())
        }
        return arr.toString()
    }

    fun deserializeStrokes(data: String?): List<CanvasStroke> {
        if (data.isNullOrBlank()) return emptyList()
        return try {
            val arr = JSONArray(data)
            val list = mutableListOf<CanvasStroke>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val stroke = CanvasStroke.fromJson(obj)
                if (stroke != null) list.add(stroke)
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}

/**
 * Main Digital Canvas Component for sketching calculus diagrams, math notes,
 * and scratchpad work directly inside notebook entries.
 */
@Composable
fun CalculusScratchpadCanvas(
    topicId: Int,
    topicTitle: String,
    initialData: String?,
    onSaveData: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val writingStyle = LocalWritingStyle.current

    // State for strokes and undo/redo stacks
    var strokes by remember(topicId, initialData) {
        mutableStateOf(CanvasSerializationHelper.deserializeStrokes(initialData))
    }
    var redoStack by remember(topicId) { mutableStateOf(listOf<CanvasStroke>()) }
    var currentStrokePoints by remember { mutableStateOf<List<CanvasPoint>?>(null) }

    // Active tool state
    var selectedTool by remember { mutableStateOf(CanvasTool.PEN) }
    var selectedColor by remember { mutableStateOf(Color(0xFF0F172A)) } // Default Ink Navy
    var strokeThickness by remember { mutableStateOf(4.5f) } // 2.5f fine, 4.5f normal, 8f marker
    var backgroundType by remember { mutableStateOf(CanvasBackground.CARTESIAN) }
    var isExpanded by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(false) }
    var showTemplatesMenu by remember { mutableStateOf(false) }
    var saveFeedbackText by remember { mutableStateOf<String?>(null) }

    // Auto-save on strokes change
    fun updateStrokesAndSave(newStrokes: List<CanvasStroke>) {
        strokes = newStrokes
        val serialized = CanvasSerializationHelper.serializeStrokes(newStrokes)
        onSaveData(serialized)
        saveFeedbackText = "Saved (${newStrokes.size} strokes)"
    }

    val canvasHeight by animateDpAsState(
        targetValue = if (isExpanded) 420.dp else 250.dp,
        label = "canvasHeight"
    )

    // Palette Colors
    val colorsList = remember {
        listOf(
            Color(0xFF0F172A), // Ink Navy
            Color(0xFF374151), // Graphite Grey
            Color(0xFF2563EB), // Calculus Blue
            Color(0xFFDC2626), // Derivative Red
            Color(0xFF16A34A), // Integral Green
            Color(0xFFD97706), // Amber
            Color(0xFF7C3AED), // Violet
            Color(0xFFFACC15)  // Highlighter Yellow
        )
    }

    // Function to add quick calculus diagram templates
    fun insertCalculusTemplate(type: String, canvasSize: Size) {
        val w = if (canvasSize.width > 0f) canvasSize.width else 600f
        val h = if (canvasSize.height > 0f) canvasSize.height else 400f
        val cx = w / 2f
        val cy = h / 2f

        val newStrokesList = strokes.toMutableList()

        when (type) {
            "axes" -> {
                // Coordinate Axes with arrows
                val axisColor = Color(0xFF475569).toArgb()
                val xPoints = listOf(CanvasPoint(20f, cy), CanvasPoint(w - 20f, cy))
                val yPoints = listOf(CanvasPoint(cx, h - 20f), CanvasPoint(cx, 20f))
                newStrokesList.add(CanvasStroke(xPoints, axisColor, 3f, isStraightLine = true))
                newStrokesList.add(CanvasStroke(yPoints, axisColor, 3f, isStraightLine = true))
            }
            "parabola" -> {
                // Smooth Parabola y = x^2 centered
                val curvePts = mutableListOf<CanvasPoint>()
                val span = (w * 0.35f)
                val steps = 40
                for (i in -steps..steps) {
                    val t = i.toFloat() / steps
                    val px = cx + t * span
                    val py = (cy + (span * 0.65f)) - (t * t * (span * 0.9f))
                    curvePts.add(CanvasPoint(px, py))
                }
                newStrokesList.add(CanvasStroke(curvePts, Color(0xFF2563EB).toArgb(), 4.5f))
            }
            "tangent" -> {
                // Parabolic curve with colored tangent line
                val curvePts = mutableListOf<CanvasPoint>()
                val span = (w * 0.3f)
                val steps = 30
                for (i in -steps..steps) {
                    val t = i.toFloat() / steps
                    val px = cx + t * span
                    val py = (cy + span * 0.4f) - (t * t * span * 0.7f)
                    curvePts.add(CanvasPoint(px, py))
                }
                newStrokesList.add(CanvasStroke(curvePts, Color(0xFF2563EB).toArgb(), 4f))

                // Tangent line touching at point (cx + span*0.4, cy + ...)
                val tx = cx + span * 0.35f
                val ty = (cy + span * 0.4f) - (0.35f * 0.35f * span * 0.7f)
                val tangentSlope = -2f * 0.35f * 0.7f
                val tPoints = listOf(
                    CanvasPoint(tx - span * 0.6f, ty - span * 0.6f * tangentSlope),
                    CanvasPoint(tx + span * 0.6f, ty + span * 0.6f * tangentSlope)
                )
                newStrokesList.add(CanvasStroke(tPoints, Color(0xFFDC2626).toArgb(), 3.5f, isStraightLine = true))
            }
            "riemann" -> {
                // Curve with 4 Riemann rectangles underneath
                val curveColor = Color(0xFF2563EB).toArgb()
                val rectColor = Color(0xFF16A34A).toArgb()
                val baseY = cy + h * 0.3f
                val startX = cx - w * 0.3f
                val endX = cx + w * 0.3f
                val rectCount = 4
                val rectWidth = (endX - startX) / rectCount

                // Add rectangles
                for (i in 0 until rectCount) {
                    val rx1 = startX + i * rectWidth
                    val rx2 = rx1 + rectWidth
                    val t = (rx1 + rectWidth * 0.5f - cx) / (w * 0.3f)
                    val topY = (cy + h * 0.1f) - (t * t * h * 0.25f)
                    val rPoints = listOf(
                        CanvasPoint(rx1, baseY),
                        CanvasPoint(rx1, topY),
                        CanvasPoint(rx2, topY),
                        CanvasPoint(rx2, baseY),
                        CanvasPoint(rx1, baseY)
                    )
                    newStrokesList.add(CanvasStroke(rPoints, rectColor, 2.5f, isHighlighter = true))
                }

                // Add bounding curve
                val curvePts = mutableListOf<CanvasPoint>()
                for (i in 0..40) {
                    val px = startX + (i.toFloat() / 40f) * (endX - startX)
                    val t = (px - cx) / (w * 0.3f)
                    val py = (cy + h * 0.1f) - (t * t * h * 0.25f)
                    curvePts.add(CanvasPoint(px, py))
                }
                newStrokesList.add(CanvasStroke(curvePts, curveColor, 4.5f))
            }
        }

        redoStack = emptyList()
        updateStrokesAndSave(newStrokesList)
    }

    // Main Scratchpad Card
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("calculus_scratchpad_card"),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFFDF8),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFE2E8F0)),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // --- TOP HEADER BAR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✍️", fontSize = 14.sp)
                    }
                    Column {
                        Text(
                            text = "Calculus Diagram & Scratchpad",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontFamily = writingStyle.headingFontFamily
                        )
                        Text(
                            text = saveFeedbackText ?: "Saved for Topic $topicId • Tap & sketch your proof/notes",
                            fontSize = 10.5.sp,
                            color = if (saveFeedbackText != null) Color(0xFF15803D) else Color(0xFF64748B),
                            fontFamily = writingStyle.bodyFontFamily
                        )
                    }
                }

                // Action controls: Expand / Fullscreen
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Size toggle button
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(28.dp).testTag("canvas_toggle_size")
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.VerticalAlignBottom else Icons.Default.VerticalAlignCenter,
                            contentDescription = "Toggle Height",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Fullscreen Dialog button
                    IconButton(
                        onClick = { isFullScreen = true },
                        modifier = Modifier.size(28.dp).testTag("canvas_open_fullscreen")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Full Screen Canvas",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- TOOLING & CONTROL STRIP ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Tool Selection Group
                CanvasTool.values().forEach { tool ->
                    val isSelected = selectedTool == tool
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedTool = tool
                            if (tool == CanvasTool.HIGHLIGHTER) {
                                selectedColor = Color(0xFFFACC15)
                                strokeThickness = 16f
                            } else if (tool == CanvasTool.PEN && strokeThickness > 10f) {
                                strokeThickness = 4.5f
                            }
                        },
                        label = {
                            Text(
                                text = tool.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = {
                            when (tool) {
                                CanvasTool.PEN -> Icon(Icons.Default.Create, null, modifier = Modifier.size(13.dp))
                                CanvasTool.HIGHLIGHTER -> Icon(Icons.Default.BorderColor, null, modifier = Modifier.size(13.dp))
                                CanvasTool.LINE -> Icon(Icons.Default.Timeline, null, modifier = Modifier.size(13.dp))
                                CanvasTool.ERASER -> Icon(Icons.Default.Clear, null, modifier = Modifier.size(13.dp))
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFE0E7FF),
                            selectedLabelColor = Color(0xFF1E1B4B)
                        ),
                        modifier = Modifier.height(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 2. Stroke Thickness Pills
                if (selectedTool != CanvasTool.ERASER) {
                    listOf(
                        2.5f to "Fine",
                        4.5f to "Mid",
                        8.0f to "Bold"
                    ).forEach { (thickness, label) ->
                        val isThicknessSelected = strokeThickness == thickness
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isThicknessSelected) Color(0xFFE2E8F0) else Color.White,
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { strokeThickness = thickness }
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isThicknessSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // 3. Background Switcher Button
                Box {
                    OutlinedButton(
                        onClick = {
                            // Cycle through background types
                            val nextIndex = (backgroundType.ordinal + 1) % CanvasBackground.values().size
                            backgroundType = CanvasBackground.values()[nextIndex]
                        },
                        contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = "${backgroundType.icon} ${backgroundType.label}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                    }
                }

                // 4. Quick Calculus Templates Menu Trigger
                Box {
                    FilledTonalButton(
                        onClick = { showTemplatesMenu = !showTemplatesMenu },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp).testTag("canvas_insert_template_btn"),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFFFEF3C7),
                            contentColor = Color(0xFF92400E)
                        )
                    ) {
                        Icon(Icons.Default.Functions, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Templates ▾", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    DropdownMenu(
                        expanded = showTemplatesMenu,
                        onDismissRequest = { showTemplatesMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("⊞ Insert XY Coordinate Axes", fontSize = 12.sp) },
                            onClick = {
                                insertCalculusTemplate("axes", Size(600f, 400f))
                                showTemplatesMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📈 Insert Parabola (y = x²)", fontSize = 12.sp) },
                            onClick = {
                                insertCalculusTemplate("parabola", Size(600f, 400f))
                                showTemplatesMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📐 Insert Curve + Tangent Slope Line", fontSize = 12.sp) },
                            onClick = {
                                insertCalculusTemplate("tangent", Size(600f, 400f))
                                showTemplatesMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📊 Insert Riemann Integral Rectangles", fontSize = 12.sp) },
                            onClick = {
                                insertCalculusTemplate("riemann", Size(600f, 400f))
                                showTemplatesMenu = false
                            }
                        )
                    }
                }

                // 5. Undo, Redo, Clear
                IconButton(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            val last = strokes.last()
                            val newStrokes = strokes.dropLast(1)
                            redoStack = redoStack + last
                            updateStrokesAndSave(newStrokes)
                        }
                    },
                    enabled = strokes.isNotEmpty(),
                    modifier = Modifier.size(28.dp).testTag("canvas_undo_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (strokes.isNotEmpty()) Color(0xFF334155) else Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (redoStack.isNotEmpty()) {
                            val restored = redoStack.last()
                            redoStack = redoStack.dropLast(1)
                            val newStrokes = strokes + restored
                            updateStrokesAndSave(newStrokes)
                        }
                    },
                    enabled = redoStack.isNotEmpty(),
                    modifier = Modifier.size(28.dp).testTag("canvas_redo_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (redoStack.isNotEmpty()) Color(0xFF334155) else Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            redoStack = redoStack + strokes
                            updateStrokesAndSave(emptyList())
                        }
                    },
                    enabled = strokes.isNotEmpty(),
                    modifier = Modifier.size(28.dp).testTag("canvas_clear_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Canvas",
                        tint = if (strokes.isNotEmpty()) Color(0xFFDC2626) else Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // --- COLOR PALETTE STRIP ---
            if (selectedTool != CanvasTool.ERASER) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ink:",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    colorsList.forEach { color ->
                        val isColorSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isColorSelected) 2.5.dp else 1.dp,
                                    color = if (isColorSelected) Color(0xFF1E293B) else Color(0xFFCBD5E1),
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = color }
                        ) {
                            if (isColorSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color.White, CircleShape)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // --- DRAWING CANVAS AREA ---
            var canvasSizeState by remember { mutableStateOf(Size.Zero) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(canvasHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("drawing_canvas_surface")
                        .pointerInput(selectedTool, selectedColor, strokeThickness) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    if (selectedTool == CanvasTool.ERASER) {
                                        // Erase strokes near point
                                        val newStrokes = strokes.filterNot { stroke ->
                                            stroke.points.any { pt ->
                                                hypot(pt.x - offset.x, pt.y - offset.y) < 26f
                                            }
                                        }
                                        if (newStrokes.size != strokes.size) {
                                            updateStrokesAndSave(newStrokes)
                                        }
                                    } else {
                                        currentStrokePoints = listOf(CanvasPoint(offset.x, offset.y))
                                    }
                                },
                                onDrag = { change, _ ->
                                    change.consume() // Consume drag so parent Column doesn't scroll!
                                    val offset = change.position
                                    if (selectedTool == CanvasTool.ERASER) {
                                        val newStrokes = strokes.filterNot { stroke ->
                                            stroke.points.any { pt ->
                                                hypot(pt.x - offset.x, pt.y - offset.y) < 26f
                                            }
                                        }
                                        if (newStrokes.size != strokes.size) {
                                            updateStrokesAndSave(newStrokes)
                                        }
                                    } else {
                                        val current = currentStrokePoints
                                        if (current != null) {
                                            if (selectedTool == CanvasTool.LINE) {
                                                // Only keep start and end
                                                currentStrokePoints = listOf(current.first(), CanvasPoint(offset.x, offset.y))
                                            } else {
                                                currentStrokePoints = current + CanvasPoint(offset.x, offset.y)
                                            }
                                        }
                                    }
                                },
                                onDragEnd = {
                                    val pts = currentStrokePoints
                                    if (!pts.isNullOrEmpty() && selectedTool != CanvasTool.ERASER) {
                                        val newStroke = CanvasStroke(
                                            points = pts,
                                            colorInt = selectedColor.toArgb(),
                                            strokeWidth = strokeThickness,
                                            isHighlighter = selectedTool == CanvasTool.HIGHLIGHTER,
                                            isStraightLine = selectedTool == CanvasTool.LINE
                                        )
                                        redoStack = emptyList()
                                        updateStrokesAndSave(strokes + newStroke)
                                    }
                                    currentStrokePoints = null
                                },
                                onDragCancel = {
                                    currentStrokePoints = null
                                }
                            )
                        }
                ) {
                    canvasSizeState = size

                    // 1. Draw Background Grid
                    drawCanvasBackground(backgroundType, size)

                    // 2. Draw Committed Strokes
                    strokes.forEach { stroke ->
                        drawSingleStroke(stroke)
                    }

                    // 3. Draw Active Stroke in Progress
                    currentStrokePoints?.let { pts ->
                        if (pts.isNotEmpty()) {
                            val activeStroke = CanvasStroke(
                                points = pts,
                                colorInt = selectedColor.toArgb(),
                                strokeWidth = strokeThickness,
                                isHighlighter = selectedTool == CanvasTool.HIGHLIGHTER,
                                isStraightLine = selectedTool == CanvasTool.LINE
                            )
                            drawSingleStroke(activeStroke)
                        }
                    }
                }

                // Empty state helper prompt
                if (strokes.isEmpty() && currentStrokePoints == null) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✍️ Scratchpad Canvas",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "Draw graph curves, test tangent slopes, or scribble working steps",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }

    // --- FULLSCREEN EXPANDED DIALOG ---
    if (isFullScreen) {
        Dialog(
            onDismissRequest = { isFullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFFDF8),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Dialog Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Full Screen Scratchpad • Chapter $topicId",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontFamily = writingStyle.headingFontFamily
                            )
                            Text(
                                text = topicTitle,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    if (strokes.isNotEmpty()) {
                                        val last = strokes.last()
                                        redoStack = redoStack + last
                                        updateStrokesAndSave(strokes.dropLast(1))
                                    }
                                },
                                enabled = strokes.isNotEmpty()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Undo, "Undo")
                            }

                            IconButton(
                                onClick = {
                                    if (redoStack.isNotEmpty()) {
                                        val restored = redoStack.last()
                                        redoStack = redoStack.dropLast(1)
                                        updateStrokesAndSave(strokes + restored)
                                    }
                                },
                                enabled = redoStack.isNotEmpty()
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Redo, "Redo")
                            }

                            FilledTonalButton(
                                onClick = { isFullScreen = false },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.FullscreenExit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Done")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dialog Tools Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CanvasTool.values().forEach { tool ->
                            FilterChip(
                                selected = selectedTool == tool,
                                onClick = { selectedTool = tool },
                                label = { Text(tool.label) }
                            )
                        }

                        Button(
                            onClick = { insertCalculusTemplate("axes", Size(1000f, 800f)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E7FF), contentColor = Color(0xFF1E1B4B))
                        ) {
                            Text("+ Axes")
                        }

                        Button(
                            onClick = { insertCalculusTemplate("tangent", Size(1000f, 800f)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF3C7), contentColor = Color(0xFF92400E))
                        ) {
                            Text("+ Tangent")
                        }

                        Button(
                            onClick = { insertCalculusTemplate("riemann", Size(1000f, 800f)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDCFCE7), contentColor = Color(0xFF15803D))
                        ) {
                            Text("+ Riemann Sum")
                        }

                        // Colors
                        colorsList.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(if (selectedColor == color) 2.5.dp else 1.dp, Color.Black, CircleShape)
                                    .clickable { selectedColor = color }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Fullscreen Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.2.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .background(Color.White)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(selectedTool, selectedColor, strokeThickness) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            if (selectedTool == CanvasTool.ERASER) {
                                                val newStrokes = strokes.filterNot { stroke ->
                                                    stroke.points.any { pt ->
                                                        hypot(pt.x - offset.x, pt.y - offset.y) < 30f
                                                    }
                                                }
                                                if (newStrokes.size != strokes.size) {
                                                    updateStrokesAndSave(newStrokes)
                                                }
                                            } else {
                                                currentStrokePoints = listOf(CanvasPoint(offset.x, offset.y))
                                            }
                                        },
                                        onDrag = { change, _ ->
                                            change.consume()
                                            val offset = change.position
                                            if (selectedTool == CanvasTool.ERASER) {
                                                val newStrokes = strokes.filterNot { stroke ->
                                                    stroke.points.any { pt ->
                                                        hypot(pt.x - offset.x, pt.y - offset.y) < 30f
                                                    }
                                                }
                                                if (newStrokes.size != strokes.size) {
                                                    updateStrokesAndSave(newStrokes)
                                                }
                                            } else {
                                                val current = currentStrokePoints
                                                if (current != null) {
                                                    if (selectedTool == CanvasTool.LINE) {
                                                        currentStrokePoints = listOf(current.first(), CanvasPoint(offset.x, offset.y))
                                                    } else {
                                                        currentStrokePoints = current + CanvasPoint(offset.x, offset.y)
                                                    }
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            val pts = currentStrokePoints
                                            if (!pts.isNullOrEmpty() && selectedTool != CanvasTool.ERASER) {
                                                val newStroke = CanvasStroke(
                                                    points = pts,
                                                    colorInt = selectedColor.toArgb(),
                                                    strokeWidth = strokeThickness,
                                                    isHighlighter = selectedTool == CanvasTool.HIGHLIGHTER,
                                                    isStraightLine = selectedTool == CanvasTool.LINE
                                                )
                                                redoStack = emptyList()
                                                updateStrokesAndSave(strokes + newStroke)
                                            }
                                            currentStrokePoints = null
                                        },
                                        onDragCancel = {
                                            currentStrokePoints = null
                                        }
                                    )
                                }
                        ) {
                            drawCanvasBackground(backgroundType, size)
                            strokes.forEach { drawSingleStroke(it) }
                            currentStrokePoints?.let { pts ->
                                if (pts.isNotEmpty()) {
                                    val activeStroke = CanvasStroke(
                                        points = pts,
                                        colorInt = selectedColor.toArgb(),
                                        strokeWidth = strokeThickness,
                                        isHighlighter = selectedTool == CanvasTool.HIGHLIGHTER,
                                        isStraightLine = selectedTool == CanvasTool.LINE
                                    )
                                    drawSingleStroke(activeStroke)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Helper to draw the chosen background grid on the Canvas
 */
private fun DrawScope.drawCanvasBackground(type: CanvasBackground, size: Size) {
    when (type) {
        CanvasBackground.CARTESIAN -> {
            val gridSpacing = 28.dp.toPx()
            val cx = size.width / 2f
            val cy = size.height / 2f

            // Faint grid lines
            val faintColor = Color(0xFFF1F5F9)
            // Vertical grid lines
            var x = cx % gridSpacing
            while (x < size.width) {
                drawLine(faintColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += gridSpacing
            }
            // Horizontal grid lines
            var y = cy % gridSpacing
            while (y < size.height) {
                drawLine(faintColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += gridSpacing
            }

            // Bold Center Axes
            val axisColor = Color(0xFF94A3B8)
            drawLine(axisColor, Offset(0f, cy), Offset(size.width, cy), strokeWidth = 1.6f)
            drawLine(axisColor, Offset(cx, 0f), Offset(cx, size.height), strokeWidth = 1.6f)

            // Tick marks on axes
            var tickX = cx + gridSpacing
            while (tickX < size.width) {
                drawLine(axisColor, Offset(tickX, cy - 4f), Offset(tickX, cy + 4f), strokeWidth = 1.2f)
                tickX += gridSpacing
            }
            tickX = cx - gridSpacing
            while (tickX > 0f) {
                drawLine(axisColor, Offset(tickX, cy - 4f), Offset(tickX, cy + 4f), strokeWidth = 1.2f)
                tickX -= gridSpacing
            }

            var tickY = cy + gridSpacing
            while (tickY < size.height) {
                drawLine(axisColor, Offset(cx - 4f, tickY), Offset(cx + 4f, tickY), strokeWidth = 1.2f)
                tickY += gridSpacing
            }
            tickY = cy - gridSpacing
            while (tickY > 0f) {
                drawLine(axisColor, Offset(cx - 4f, tickY), Offset(cx + 4f, tickY), strokeWidth = 1.2f)
                tickY -= gridSpacing
            }
        }
        CanvasBackground.LINED -> {
            val lineSpacing = 24.dp.toPx()
            val blueLineColor = Color(0xFFE2E8F0)
            var y = lineSpacing
            while (y < size.height) {
                drawLine(blueLineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += lineSpacing
            }
            // Margin line in faint pink/red
            val marginX = 36.dp.toPx()
            drawLine(Color(0xFFFCA5A5), Offset(marginX, 0f), Offset(marginX, size.height), strokeWidth = 1.2f)
        }
        CanvasBackground.DOTS -> {
            val dotSpacing = 22.dp.toPx()
            val dotColor = Color(0xFFCBD5E1)
            var x = dotSpacing
            while (x < size.width) {
                var y = dotSpacing
                while (y < size.height) {
                    drawCircle(dotColor, radius = 1.5f, center = Offset(x, y))
                    y += dotSpacing
                }
                x += dotSpacing
            }
        }
        CanvasBackground.BLANK -> {
            // Pure clean canvas
        }
    }
}

/**
 * Draws a single stroke with smoothing and proper cap/join
 */
private fun DrawScope.drawSingleStroke(stroke: CanvasStroke) {
    if (stroke.points.isEmpty()) return

    val strokeColor = if (stroke.isHighlighter) {
        Color(stroke.colorInt).copy(alpha = 0.38f)
    } else {
        Color(stroke.colorInt)
    }

    val widthPx = stroke.strokeWidth.dp.toPx()

    if (stroke.isStraightLine && stroke.points.size >= 2) {
        val start = stroke.points.first().toOffset()
        val end = stroke.points.last().toOffset()
        drawLine(
            color = strokeColor,
            start = start,
            end = end,
            strokeWidth = widthPx,
            cap = StrokeCap.Round
        )
        return
    }

    if (stroke.points.size == 1) {
        drawCircle(
            color = strokeColor,
            radius = widthPx / 2f,
            center = stroke.points.first().toOffset()
        )
        return
    }

    val path = Path().apply {
        val first = stroke.points.first()
        moveTo(first.x, first.y)

        // Smooth curve with quadratic midpoint smoothing
        for (i in 1 until stroke.points.size) {
            val prev = stroke.points[i - 1]
            val curr = stroke.points[i]
            val midX = (prev.x + curr.x) / 2f
            val midY = (prev.y + curr.y) / 2f
            quadraticTo(prev.x, prev.y, midX, midY)
        }
        val last = stroke.points.last()
        lineTo(last.x, last.y)
    }

    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(
            width = widthPx,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
