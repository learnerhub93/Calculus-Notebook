package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.PartQuiz
import com.example.ui.theme.LocalWritingStyle

@Composable
fun QuizDialog(
    quiz: PartQuiz,
    onSaveScore: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val writingStyle = LocalWritingStyle.current
    // Map of question index -> selected option index
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var isSubmitted by remember { mutableStateOf(false) }

    val score = remember(isSubmitted) {
        if (!isSubmitted) 0
        else {
            quiz.questions.indices.count { idx ->
                selectedAnswers[idx] == quiz.questions[idx].correctIndex
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(12.dp),
            color = PaperCream,
            border = androidx.compose.foundation.BorderStroke(2.dp, InkNavy)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with Teacher's Red Pen Grade
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📝 ${quiz.partTitle}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = writingStyle.headingFontFamily
                        )
                        Text(
                            text = if (isSubmitted) "Grade: $score / ${quiz.questions.size} Points" else "Select the best answer for each question",
                            fontSize = 12.sp,
                            color = if (isSubmitted) CorrectionRed else InkCharcoal,
                            fontWeight = if (isSubmitted) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = InkDark)
                    }
                }

                if (isSubmitted) {
                    // Teacher Praise Badge
                    val praise = when {
                        score == quiz.questions.size -> "🌟 Perfect Score! Gold Star Calculus Master!"
                        score >= quiz.questions.size - 1 -> "👏 Excellent work! Very solid understanding."
                        else -> "✎ Good practice! Review the explanations below."
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(HighlighterYellow, RoundedCornerShape(6.dp))
                            .border(1.dp, Color(0xFFFBBF24), RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = praise,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Questions List
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    itemsIndexed(quiz.questions) { qIdx, question ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFFDF8),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Q${qIdx + 1}. ${question.question}",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkNavy,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                question.options.forEachIndexed { optIdx, optionText ->
                                    val isSelected = selectedAnswers[qIdx] == optIdx
                                    val isCorrect = question.correctIndex == optIdx

                                    val rowBg = when {
                                        !isSubmitted && isSelected -> HighlighterYellow
                                        isSubmitted && isCorrect -> Color(0xFFDCFCE7) // Green for correct answer
                                        isSubmitted && isSelected && !isCorrect -> Color(0xFFFEE2E2) // Red for wrong selected
                                        else -> Color.Transparent
                                    }

                                    val borderCol = when {
                                        isSubmitted && isCorrect -> Color(0xFF16A34A)
                                        isSubmitted && isSelected && !isCorrect -> CorrectionRed
                                        isSelected -> Color(0xFFF59E0B)
                                        else -> Color(0xFFE2E8F0)
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(rowBg)
                                            .border(1.dp, borderCol, RoundedCornerShape(6.dp))
                                            .clickable(enabled = !isSubmitted) {
                                                selectedAnswers[qIdx] = optIdx
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Option letter circle A, B, C, D
                                        val letter = ('A' + optIdx).toString()
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .background(
                                                    if (isSelected) InkNavy else Color(0xFFF1F5F9),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = letter,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else InkNavy
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = optionText,
                                            fontSize = 12.5.sp,
                                            color = InkDark,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (isSubmitted && isCorrect) {
                                            Text("✓ Correct", color = Color(0xFF16A34A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        } else if (isSubmitted && isSelected && !isCorrect) {
                                            Text("✗ Incorrect", color = CorrectionRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                // Explanation after submission
                                if (isSubmitted) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Teacher's note: ${question.explanation}",
                                        fontSize = 11.5.sp,
                                        color = InkNavy,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action buttons: Submit or Close
                if (!isSubmitted) {
                    Button(
                        onClick = {
                            isSubmitted = true
                            val finalScore = quiz.questions.indices.count { idx ->
                                selectedAnswers[idx] == quiz.questions[idx].correctIndex
                            }
                            onSaveScore(finalScore)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                        shape = RoundedCornerShape(8.dp),
                        enabled = selectedAnswers.size == quiz.questions.size
                    ) {
                        Text(
                            text = if (selectedAnswers.size == quiz.questions.size) "Grade My Quiz ✍️" else "Answer All Questions (${selectedAnswers.size}/${quiz.questions.size})",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Return to Notebook", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
