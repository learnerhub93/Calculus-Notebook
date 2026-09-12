package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalculusCurriculum
import com.example.model.Topic
import com.example.ui.theme.*

enum class SidebarFilter {
    ALL,
    COMPLETED,
    REMAINING
}

/**
 * CourseSidebarContent: Comprehensive progress tracking sidebar providing a rich
 * visual representation of the student's learning journey and highlighting completed lessons.
 */
@Composable
fun CourseSidebarContent(
    currentTopicId: Int,
    unlockedTopicIds: Set<Int>,
    completedTopicIds: Set<Int>,
    studyMode: Boolean,
    onToggleStudyMode: (Boolean) -> Unit,
    onSelectTopic: (Int) -> Unit,
    onOpenQuiz: (Int) -> Unit,
    onOpenWritingStyles: () -> Unit,
    onClose: () -> Unit,
    onGoHome: (() -> Unit)? = null,
    onOpenSearch: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val writingStyle = LocalWritingStyle.current
    var selectedFilter by remember { mutableStateOf(SidebarFilter.ALL) }

    val totalTopics = CalculusCurriculum.topics.size // 25
    val completedCount = completedTopicIds.size
    val progressFraction = (completedCount.toFloat() / totalTopics.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(500),
        label = "sidebar_progress"
    )

    // Milestone rank badge based on completed lessons
    val (rankTitle, rankIcon, rankColor) = when {
        completedCount == 25 -> Triple("Calculus Polymath", "👑", Color(0xFFEAB308))
        completedCount >= 20 -> Triple("Integration Master", "🏆", Color(0xFF10B981))
        completedCount >= 15 -> Triple("Optimization Scholar", "📐", Color(0xFF3B82F6))
        completedCount >= 10 -> Triple("Derivative Virtuoso", "📈", Color(0xFF8B5CF6))
        completedCount >= 5 -> Triple("Limit Conqueror", "⚡", Color(0xFFF97316))
        else -> Triple("Curious Apprentice", "🌱", Color(0xFF059669))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperCream)
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("course_sidebar")
    ) {
        // --- 1. Top Header: Title, Watermark & Close Button ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "🧭", fontSize = 20.sp)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Learning Journey",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = writingStyle.headingFontFamily
                        )
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            color = Color(0x121E3A8A),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0x221E3A8A))
                        ) {
                            Text(
                                text = "by vivek",
                                fontSize = 9.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold,
                                color = InkNavy.copy(alpha = 0.75f),
                                fontFamily = writingStyle.noteFontFamily,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Calculus Course Directory",
                        fontSize = 10.5.sp,
                        color = InkCharcoal
                    )
                }
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Sidebar",
                    tint = InkNavy,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (onGoHome != null) {
            Spacer(modifier = Modifier.height(8.dp))
            FilledTonalButton(
                onClick = onGoHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFE0E7FF),
                    contentColor = InkNavy
                )
            ) {
                Text(text = "🏠", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Go to Topics Home Page",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = writingStyle.headingFontFamily
                )
            }
        }

        if (onOpenSearch != null) {
            Spacer(modifier = Modifier.height(6.dp))
            FilledTonalButton(
                onClick = onOpenSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .testTag("sidebar_search_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFFEF3C7),
                    contentColor = Color(0xFF92400E)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Search Equations & Terms",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = writingStyle.headingFontFamily
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- 2. Visual Representation Card: Journey Progress Overview ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF0FDF4),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Top Row: Mastery count + Stage Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Course Mastery",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534),
                            fontFamily = writingStyle.headingFontFamily
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "$completedCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF15803D)
                            )
                            Text(
                                text = "/ $totalTopics completed",
                                fontSize = 11.5.sp,
                                color = InkCharcoal,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }

                    // Level Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFF86EFAC))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = rankIcon, fontSize = 12.sp)
                            Text(
                                text = rankTitle,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Animated Progress Bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(RoundedCornerShape(3.5.dp)),
                    color = Color(0xFF16A34A),
                    trackColor = Color(0xFFDCFCE7)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Chapter Milestone Map: 5 Part Nodes connected with lines
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CalculusCurriculum.parts.forEachIndexed { idx, part ->
                        val partTopics = CalculusCurriculum.topics.filter { it.partId == part.id }
                        val partCompleted = partTopics.count { completedTopicIds.contains(it.id) }
                        val isPartAllDone = partCompleted == partTopics.size && partTopics.isNotEmpty()
                        val isPartStarted = partCompleted > 0

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isPartAllDone -> Color(0xFF16A34A)
                                            isPartStarted -> Color(0xFF3B82F6)
                                            else -> Color(0xFFE2E8F0)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPartAllDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Part ${idx + 1} Done",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                } else {
                                    Text(
                                        text = "P${idx + 1}",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPartStarted) Color.White else InkCharcoal
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$partCompleted/${partTopics.size}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isPartAllDone) Color(0xFF16A34A) else InkCharcoal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 3. Filter Chips: All, Completed, Remaining ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilter == SidebarFilter.ALL,
                onClick = { selectedFilter = SidebarFilter.ALL },
                label = { Text("All ($totalTopics)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFE0E7FF),
                    selectedLabelColor = InkNavy
                ),
                modifier = Modifier.height(30.dp)
            )

            FilterChip(
                selected = selectedFilter == SidebarFilter.COMPLETED,
                onClick = { selectedFilter = SidebarFilter.COMPLETED },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("✓ Done ($completedCount)", fontSize = 11.sp)
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFDCFCE7),
                    selectedLabelColor = Color(0xFF15803D)
                ),
                modifier = Modifier.height(30.dp)
            )

            FilterChip(
                selected = selectedFilter == SidebarFilter.REMAINING,
                onClick = { selectedFilter = SidebarFilter.REMAINING },
                label = { Text("Remaining (${totalTopics - completedCount})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFEF3C7),
                    selectedLabelColor = Color(0xFF92400E)
                ),
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- 4. Study Mode & Handwriting Settings Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF9C3), RoundedCornerShape(8.dp))
                .border(0.8.dp, Color(0xFFFDE047), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Free Study Mode",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkNavy
                )
                Text(
                    text = if (studyMode) "All chapters unlocked" else "Sequential curriculum flow",
                    fontSize = 9.5.sp,
                    color = InkCharcoal
                )
            }
            Switch(
                checked = studyMode,
                onCheckedChange = onToggleStudyMode,
                modifier = Modifier.height(26.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = InkNavy,
                    checkedTrackColor = HighlighterYellowSolid
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- 5. Chapter & Lesson List with Progress Highlighting ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalculusCurriculum.parts.forEach { part ->
                val allTopicsInPart = CalculusCurriculum.topics.filter { it.partId == part.id }
                val topicsInPart = allTopicsInPart.filter { topic ->
                    when (selectedFilter) {
                        SidebarFilter.ALL -> true
                        SidebarFilter.COMPLETED -> completedTopicIds.contains(topic.id)
                        SidebarFilter.REMAINING -> !completedTopicIds.contains(topic.id)
                    }
                }

                if (topicsInPart.isNotEmpty()) {
                    val partCompleted = allTopicsInPart.count { completedTopicIds.contains(it.id) }
                    val isPartMastered = partCompleted == allTopicsInPart.size && allTopicsInPart.isNotEmpty()
                    val partFraction = (partCompleted.toFloat() / allTopicsInPart.size.toFloat()).coerceIn(0f, 1f)

                    // Chapter / Part Header Card with Progress Highlight
                    item(key = "part_${part.id}") {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            color = if (isPartMastered) Color(0xFFDCFCE7) else Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isPartMastered) Color(0xFF86EFAC) else Color(0xFFBFDBFE)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = part.title,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPartMastered) Color(0xFF166534) else InkNavy,
                                            fontFamily = writingStyle.headingFontFamily
                                        )
                                    }

                                    // Part Mastery Status Pill
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isPartMastered) Color(0xFF16A34A) else Color(0xFFDBEAFE)
                                    ) {
                                        Text(
                                            text = if (isPartMastered) "★ Mastered ($partCompleted/${allTopicsInPart.size})"
                                            else "$partCompleted/${allTopicsInPart.size}",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPartMastered) Color.White else InkNavy,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Chapter mini progress bar
                                LinearProgressIndicator(
                                    progress = { partFraction },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp)),
                                    color = if (isPartMastered) Color(0xFF16A34A) else Color(0xFF3B82F6),
                                    trackColor = Color(0x33CBD5E1)
                                )
                            }
                        }
                    }

                    // Lessons inside this part
                    items(topicsInPart, key = { it.id }) { topic ->
                        val isUnlocked = studyMode || unlockedTopicIds.contains(topic.id)
                        val isCurrent = topic.id == currentTopicId
                        val isCompleted = completedTopicIds.contains(topic.id)

                        // Highlight completed lessons with vivid green accent and badge
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = isUnlocked) {
                                    onSelectTopic(topic.id)
                                    onClose()
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isCurrent -> Color(0xFFFEF9C3) // Yellow highlighter for active
                                isCompleted -> Color(0xFFF0FDF4) // Green highlighter for completed
                                isUnlocked -> Color(0xFFFFFFFF) // Clean paper for unlocked
                                else -> Color(0xFFF8FAFC) // Slate for locked
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isCurrent) 1.5.dp else if (isCompleted) 1.dp else 0.7.dp,
                                color = when {
                                    isCurrent -> Color(0xFFF59E0B)
                                    isCompleted -> Color(0xFF86EFAC)
                                    isUnlocked -> Color(0xFFE2E8F0)
                                    else -> Color(0xFFF1F5F9)
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Status Indicator
                                if (isCompleted) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF16A34A)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Lesson Completed",
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                } else if (isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF59E0B)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${topic.chapterNumber}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                } else if (isUnlocked) {
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF1F5F9))
                                            .border(0.8.dp, Color(0xFFCBD5E1), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${topic.chapterNumber}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = InkNavy
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Topic Title & Status Subtitle
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = topic.shortTitle,
                                        fontSize = 12.sp,
                                        fontWeight = if (isCurrent || isCompleted) FontWeight.Bold else FontWeight.Medium,
                                        color = when {
                                            isCompleted -> Color(0xFF15803D)
                                            isUnlocked -> InkNavy
                                            else -> Color(0xFF94A3B8)
                                        }
                                    )
                                    Text(
                                        text = when {
                                            isCurrent -> "Active Lesson • Chapter ${topic.chapterNumber}"
                                            isCompleted -> "✓ Mastered & Completed"
                                            isUnlocked -> "Ready to study"
                                            else -> "Prerequisite required"
                                        },
                                        fontSize = 9.5.sp,
                                        color = when {
                                            isCurrent -> Color(0xFFB45309)
                                            isCompleted -> Color(0xFF16A34A)
                                            else -> Color(0xFF64748B)
                                        }
                                    )
                                }

                                // Trailing Highlight Tag
                                if (isCompleted) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFDCFCE7),
                                        border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(0xFF86EFAC))
                                    ) {
                                        Text(
                                            text = "Done ✓",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D),
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                } else if (isCurrent) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFFEF08A),
                                        border = androidx.compose.foundation.BorderStroke(0.6.dp, Color(0xFFFBBF24))
                                    ) {
                                        Text(
                                            text = "Current 👈",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CorrectionRed,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // --- 6. Bottom Handwriting Font Shortcut ---
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onClose()
                    onOpenWritingStyles()
                },
            color = Color(0xFFFFF7ED),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFFED7AA))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = writingStyle.icon, fontSize = 14.sp)
                    Text(
                        text = "Font: ${writingStyle.displayName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9A3412),
                        fontFamily = writingStyle.headingFontFamily
                    )
                }
                Text(
                    text = "Customize ➔",
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC2410C)
                )
            }
        }
    }
}
