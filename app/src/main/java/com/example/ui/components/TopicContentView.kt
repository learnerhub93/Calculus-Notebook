package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Topic
import com.example.ui.components.math.LatexMathView
import com.example.ui.theme.LocalWritingStyle

@Composable
fun TopicContentView(
    topic: Topic,
    isCompleted: Boolean,
    onMarkCompleted: () -> Unit,
    onPreviousTopic: () -> Unit,
    onNextTopic: () -> Unit,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onStartQuiz: (() -> Unit)? = null,
    sketchData: String? = null,
    onSaveSketch: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val writingStyle = LocalWritingStyle.current
    var showSolution by remember(topic.id) { mutableStateOf(false) }
    var showHint by remember(topic.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // --- 1. Chapter Header & Tag ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Sketched pill for Part
                Box(
                    modifier = Modifier
                        .background(HighlighterYellow, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = topic.partTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkNavy,
                        fontFamily = writingStyle.headingFontFamily
                    )
                }

                Text(
                    text = "• by vivek",
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    color = PencilLead.copy(alpha = 0.55f),
                    fontFamily = writingStyle.noteFontFamily
                )
            }

            // Completed Checkmark Indicator
            if (isCompleted) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Mastered",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = writingStyle.headingFontFamily
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Topic Title
        Text(
            text = "Chapter ${topic.chapterNumber}: ${topic.title}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = InkNavy,
            fontFamily = writingStyle.headingFontFamily,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. Plain-English Intuition (BEFORE formulas!) ---
        Text(
            text = "📖 The Big Picture Intuition",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CorrectionRed,
            fontFamily = writingStyle.headingFontFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = topic.plainEnglishIntuition,
            fontSize = 14.sp,
            color = InkDark,
            lineHeight = 22.sp,
            fontFamily = writingStyle.bodyFontFamily
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 3. Hand-Boxed Formula Card ---
        HighlightedFormulaCard(
            title = topic.formulaTitle,
            formula = topic.formulaLatex,
            explanation = topic.formulaExplanation
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- 4. Sketched Diagram / Interactive Widget ---
        DiagramRenderer(
            diagramType = topic.diagramType,
            caption = topic.diagramCaption
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 5. Fully Worked Step-by-Step Example ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF9FAFB),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "✍️", fontSize = 16.sp)
                    Text(
                        text = topic.exampleTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkNavy,
                        fontFamily = writingStyle.headingFontFamily
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Problem: ${topic.exampleProblem}",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkCharcoal,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontFamily = writingStyle.bodyFontFamily
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Steps enumeration
                topic.exampleSteps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Step number badge
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color(0xFFE0E7FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${step.stepNumber}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkNavy,
                                fontFamily = writingStyle.headingFontFamily
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = step.explanation,
                                fontSize = 13.sp,
                                color = InkDark,
                                fontWeight = FontWeight.Medium,
                                fontFamily = writingStyle.bodyFontFamily
                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                LatexMathView(
                                    latex = step.mathExpression,
                                    fontSize = 13.sp,
                                    textColor = InkNavy,
                                    mathFontFamily = writingStyle.mathFontFamily
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 6. Try It Yourself Practice Problem with Toggle ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF0FDF4),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎯 Try It Yourself!",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534),
                            fontFamily = writingStyle.headingFontFamily
                        )
                    }
                    // Hint Toggle button
                    TextButton(
                        onClick = { showHint = !showHint },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Hint", modifier = Modifier.size(14.dp), tint = Color(0xFF166534))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showHint) "Hide Hint" else "Need Hint?",
                            fontSize = 11.5.sp,
                            color = Color(0xFF166534),
                            fontFamily = writingStyle.headingFontFamily
                        )
                    }
                }

                Text(
                    text = topic.practiceProblem.prompt,
                    fontSize = 13.5.sp,
                    color = InkDark,
                    lineHeight = 19.sp,
                    fontFamily = writingStyle.bodyFontFamily
                )

                // Hint reveal
                AnimatedVisibility(visible = showHint) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0xFFDCFCE7), RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "💡 Hint: ${topic.practiceProblem.hint}",
                            fontSize = 12.sp,
                            color = Color(0xFF15803D),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontFamily = writingStyle.bodyFontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reveal Solution Toggle Button
                OutlinedButton(
                    onClick = { showSolution = !showSolution },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF15803D)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Icon(
                        imageVector = if (showSolution) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showSolution) "Hide Step-by-Step Solution" else "Reveal Step-by-Step Solution",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = writingStyle.headingFontFamily
                    )
                }

                // Solution steps revealed
                AnimatedVisibility(
                    visible = showSolution,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(Color.White, RoundedCornerShape(6.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Step-by-Step Solution:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = writingStyle.headingFontFamily
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        topic.practiceProblem.solutionSteps.forEach { stepText ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                                Text(
                                    text = stepText,
                                    fontSize = 12.5.sp,
                                    color = InkDark,
                                    fontFamily = writingStyle.mathFontFamily
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .background(HighlighterYellow, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Final Answer: ${topic.practiceProblem.finalAnswer}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkNavy,
                                fontFamily = writingStyle.headingFontFamily
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 7. Digital Scratchpad & Diagram Canvas ---
        CalculusScratchpadCanvas(
            topicId = topic.id,
            topicTitle = topic.title,
            initialData = sketchData,
            onSaveData = { data ->
                onSaveSketch?.invoke(data)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(14.dp))

        // --- 8. Sticky Note Callout for Common Mistake ---
        StickyNoteCard(
            title = "Common Student Mistake!",
            content = topic.commonMistake,
            rotationAngle = -1.2f,
            isTip = false
        )

        // Teacher Tip Note
        StickyNoteCard(
            title = "Teacher's Pocket Secret",
            content = topic.teacherTip,
            rotationAngle = 1.0f,
            isTip = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- 8. Mark Page Complete & Quiz Trigger ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onMarkCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) Color(0xFF059669) else InkNavy
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCompleted) "✓ Page Completed & Mastered" else "Mark Page as Understood",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Optional Quiz prompt at end of part
        if (onStartQuiz != null) {
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedButton(
                onClick = onStartQuiz,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CorrectionRed
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CorrectionRed)
            ) {
                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ready? Test Yourself on ${topic.partTitle} Quiz ➔",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        SketchedDivider()
        Spacer(modifier = Modifier.height(14.dp))

        // --- 9. Page Turn Navigation Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPreviousTopic,
                enabled = hasPrevious,
                shape = RoundedCornerShape(6.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Turn Back", fontSize = 12.5.sp)
            }

            Button(
                onClick = onNextTopic,
                enabled = hasNext,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
            ) {
                Text("Turn Next Page", fontSize = 12.5.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
            }
        }
    }
}
