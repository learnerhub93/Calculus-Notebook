package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WritingStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WritingStyleSheet(
    currentStyle: WritingStyle,
    onSelectStyle: (WritingStyle) -> Unit,
    onClose: () -> Unit
) {
    var scratchpadText by remember { mutableStateOf("f'(x) = 2x + 5  [Calculus is fun!]") }

    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = PaperCream,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = PencilLead)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 6.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✍️", fontSize = 24.sp)
                    Column {
                        Text(
                            text = "Writing Style & Font",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = currentStyle.headingFontFamily
                        )
                        Text(
                            text = "Change the handwriting style for your notebook",
                            fontSize = 12.sp,
                            color = InkCharcoal
                        )
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = InkDark)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Active Tool Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentStyle.icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Current Pen: ${currentStyle.displayName} (${currentStyle.toolName})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFD97706), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Active",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Style List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(WritingStyle.entries) { style ->
                    val isSelected = style == currentStyle
                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) InkNavy else Color(0xFFE2E8F0),
                        label = "card_border"
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectStyle(style) }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(10.dp)),
                        color = if (isSelected) Color(0xFFFFFEEA) else Color.White,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            // Top Row: Icon, Title, Category Badge, and Check/Radio
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .background(
                                                if (isSelected) HighlighterYellowSolid else Color(0xFFF1F5F9),
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = style.icon, fontSize = 18.sp)
                                    }

                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = style.displayName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = InkNavy,
                                                fontFamily = style.headingFontFamily
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .background(Color(0xFFE0E7FF), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = style.categoryName,
                                                    fontSize = 9.5.sp,
                                                    color = InkNavy,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Writing Tool: ${style.toolName}",
                                            fontSize = 11.sp,
                                            color = InkCharcoal
                                        )
                                    }
                                }

                                // Selection checkmark circle
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            if (isSelected) InkNavy else Color(0xFFF1F5F9),
                                            CircleShape
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) InkNavy else Color(0xFFCBD5E1),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = style.description,
                                fontSize = 11.5.sp,
                                color = InkCharcoal,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Live sample preview box inside the card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFDFBF7), RoundedCornerShape(6.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(6.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text(
                                        text = "Chapter 4: Instantaneous Rate of Change",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = InkNavy,
                                        fontFamily = style.headingFontFamily
                                    )
                                    Text(
                                        text = style.sampleSnippet,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF1E3A8A),
                                        fontFamily = style.mathFontFamily
                                    )
                                    Text(
                                        text = "“Slope of the tangent line as secant interval approaches zero.”",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569),
                                        fontFamily = style.bodyFontFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive "Try Your Pen" Scratchpad
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = InkNavy
                        )
                        Text(
                            text = "Pen Scratchpad (Test Your Font)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = scratchpadText,
                        onValueChange = { scratchpadText = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 13.sp,
                            fontFamily = currentStyle.bodyFontFamily,
                            color = InkNavy
                        ),
                        placeholder = {
                            Text("Type calculus notes here...", fontSize = 12.sp)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = InkNavy,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Previewing in: ${currentStyle.displayName} (${currentStyle.categoryName})",
                        fontSize = 10.5.sp,
                        color = InkCharcoal,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Apply & Close Button
            Button(
                onClick = onClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
            ) {
                Text(
                    text = "Apply Writing Style ➔",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
