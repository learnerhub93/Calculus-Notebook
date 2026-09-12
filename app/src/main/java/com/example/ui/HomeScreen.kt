package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalculusCurriculum
import com.example.model.Part
import com.example.model.Topic
import com.example.ui.components.*
import com.example.ui.components.math.LatexMathView
import com.example.ui.theme.LocalWritingStyle

// Part Tab color mappings matching notebook sticky tabs
val partAccentColors = listOf(
    Color(0xFFFEF08A), // Part 1 Yellow
    Color(0xFFBAE6FD), // Part 2 Sky Blue
    Color(0xFFFED7AA), // Part 3 Orange/Peach
    Color(0xFFBBF7D0), // Part 4 Mint Green
    Color(0xFFE9D5FF)  // Part 5 Lavender
)

val partDarkAccents = listOf(
    Color(0xFFB45309), // Amber dark
    Color(0xFF0284C7), // Sky dark
    Color(0xFFEA580C), // Orange dark
    Color(0xFF16A34A), // Green dark
    Color(0xFF9333EA)  // Purple dark
)

/**
 * HomeScreen: Displays the Calculus Curriculum overview with all 25 topics
 * grouped by parts, progress gauges, intuitive previews, and instant 1-tap links
 * into the interactive Calculus Notebook.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentTopicId: Int,
    unlockedTopicIds: Set<Int>,
    completedTopicIds: Set<Int>,
    studyMode: Boolean,
    onSelectTopic: (Int) -> Unit,
    onOpenNotebook: () -> Unit,
    onOpenQuiz: (Int) -> Unit,
    onOpenWritingStyles: () -> Unit,
    onToggleStudyMode: (Boolean) -> Unit,
    onToggleTopicCompleted: ((Int) -> Unit)? = null,
    onOpenSearch: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val writingStyle = LocalWritingStyle.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedPartId by remember { mutableStateOf<Int?>(null) } // null = All Parts

    val totalTopics = CalculusCurriculum.topics.size
    val completedCount = completedTopicIds.size
    val progressFraction = (completedCount.toFloat() / totalTopics.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(600),
        label = "home_progress"
    )

    val currentTopic = remember(currentTopicId) {
        CalculusCurriculum.getTopic(currentTopicId)
    }

    // Comprehensive multi-property search across terms, definitions, equations, examples, and tips
    val filteredTopics = remember(searchQuery, selectedPartId, unlockedTopicIds, studyMode) {
        CalculusCurriculum.topics.filter { topic ->
            val matchesPart = selectedPartId == null || topic.partId == selectedPartId
            val query = searchQuery.trim()
            val matchesSearch = query.isBlank() ||
                    topic.title.contains(query, ignoreCase = true) ||
                    topic.shortTitle.contains(query, ignoreCase = true) ||
                    topic.plainEnglishIntuition.contains(query, ignoreCase = true) ||
                    topic.formulaTitle.contains(query, ignoreCase = true) ||
                    topic.formulaLatex.contains(query, ignoreCase = true) ||
                    topic.formulaExplanation.contains(query, ignoreCase = true) ||
                    topic.exampleProblem.contains(query, ignoreCase = true) ||
                    topic.exampleTitle.contains(query, ignoreCase = true) ||
                    topic.exampleSteps.any { it.explanation.contains(query, ignoreCase = true) || it.mathExpression.contains(query, ignoreCase = true) } ||
                    topic.practiceProblem.prompt.contains(query, ignoreCase = true) ||
                    topic.practiceProblem.solutionSteps.any { it.contains(query, ignoreCase = true) } ||
                    topic.teacherTip.contains(query, ignoreCase = true) ||
                    topic.commonMistake.contains(query, ignoreCase = true)
            matchesPart && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PaperCream)
                    .statusBarsPadding()
            ) {
                // Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo + App Name + 'by vivek' watermark
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "📓", fontSize = 20.sp)
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Calculus Notebook",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = InkNavy,
                                    fontFamily = writingStyle.headingFontFamily
                                )

                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = Color(0x121E3A8A),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0x221E3A8A)),
                                    modifier = Modifier.testTag("home_watermark_by_vivek")
                                ) {
                                    Text(
                                        text = "by vivek",
                                        fontSize = 9.5.sp,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.SemiBold,
                                        color = InkNavy.copy(alpha = 0.75f),
                                        fontFamily = writingStyle.noteFontFamily,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Handwritten Course • 25 Interactive Topics",
                                fontSize = 11.sp,
                                color = InkCharcoal
                            )
                        }
                    }

                    // Top Right Quick Actions
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Button
                        if (onOpenSearch != null) {
                            IconButton(
                                onClick = onOpenSearch,
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(Color(0xFFE0E7FF), RoundedCornerShape(8.dp))
                                    .testTag("home_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search terms, equations & definitions",
                                    tint = InkNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Font Style Switcher
                        FilledTonalButton(
                            onClick = onOpenWritingStyles,
                            contentPadding = PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color(0xFFFEF3C7),
                                contentColor = Color(0xFF92400E)
                            )
                        ) {
                            Text(text = writingStyle.icon, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Font",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = writingStyle.headingFontFamily
                            )
                        }

                        // Open Notebook Primary Link Button
                        Button(
                            onClick = onOpenNotebook,
                            contentPadding = PaddingValues(horizontal = 11.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(34.dp).testTag("home_open_notebook_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = InkNavy,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Open Notebook",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Notebook",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = writingStyle.headingFontFamily
                            )
                        }
                    }
                }

                HorizontalDivider(color = RuledBlue, thickness = 1.dp)
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenNotebook,
                containerColor = InkNavy,
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .testTag("home_fab_open_notebook")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open Notebook 📓",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = writingStyle.headingFontFamily
                )
            }
        },
        containerColor = PaperCream
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NotebookPaperBackground(showSpiral = false, showMarginLine = false)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
            ) {
                // --- HERO CARD: Notebook Cover & Resume Section ---
                item {
                    HeroNotebookCoverCard(
                        completedCount = completedCount,
                        totalTopics = totalTopics,
                        progressFraction = animatedProgress,
                        currentTopic = currentTopic,
                        studyMode = studyMode,
                        onToggleStudyMode = onToggleStudyMode,
                        completedTopicIds = completedTopicIds,
                        onSelectPart = { partId ->
                            selectedPartId = if (selectedPartId == partId) null else partId
                        },
                        onResumeTopic = {
                            onSelectTopic(currentTopic.id)
                        }
                    )
                }

                // --- SEARCH & PART FILTER BAR ---
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Search Row with Text Field and Deep Equation Search Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        text = "Search terms, definitions, equations...",
                                        fontSize = 13.sp,
                                        color = InkCharcoal.copy(alpha = 0.6f)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = InkNavy,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Clear search",
                                                tint = InkCharcoal,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = InkNavy,
                                    unfocusedBorderColor = Color(0xFFCBD5E1)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .testTag("home_search_input")
                            )

                            if (onOpenSearch != null) {
                                FilledTonalButton(
                                    onClick = onOpenSearch,
                                    contentPadding = PaddingValues(horizontal = 9.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .height(50.dp)
                                        .testTag("home_equation_search_btn"),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = Color(0xFFFEF3C7),
                                        contentColor = Color(0xFF92400E)
                                    )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Functions,
                                            contentDescription = "Equation Search",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Formulas",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = writingStyle.headingFontFamily
                                        )
                                    }
                                }
                            }
                        }

                        // Quick Term suggestion pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quick find:",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                            listOf("Chain Rule", "Limits", "dy/dx", "Riemann Sum", "Power Rule", "Product Rule", "Taylor Series", "Optimization").forEach { term ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (searchQuery.equals(term, ignoreCase = true)) Color(0xFFE0E7FF) else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        0.7.dp,
                                        if (searchQuery.equals(term, ignoreCase = true)) InkNavy else Color(0xFFCBD5E1)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            searchQuery = if (searchQuery.equals(term, ignoreCase = true)) "" else term
                                        }
                                ) {
                                    Text(
                                        text = term,
                                        fontSize = 11.sp,
                                        fontWeight = if (searchQuery.equals(term, ignoreCase = true)) FontWeight.Bold else FontWeight.Normal,
                                        color = if (searchQuery.equals(term, ignoreCase = true)) InkNavy else Color(0xFF334155),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Part Filter Chips Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // "All Topics" Chip
                            FilterChip(
                                selected = selectedPartId == null,
                                onClick = { selectedPartId = null },
                                label = {
                                    Text(
                                        text = "All Topics ($completedCount/$totalTopics)",
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedPartId == null) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = InkNavy,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color(0xFFF1F5F9),
                                    labelColor = InkNavy
                                )
                            )

                            // 5 Individual Part Chips with completion badges
                            CalculusCurriculum.parts.forEachIndexed { index, part ->
                                val isSelected = selectedPartId == part.id
                                val chipColor = partAccentColors[index % partAccentColors.size]
                                val partTopics = CalculusCurriculum.topics.filter { it.partId == part.id }
                                val partCompleted = partTopics.count { completedTopicIds.contains(it.id) }
                                val isPartFullyComplete = partTopics.isNotEmpty() && partCompleted == partTopics.size

                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            selectedPartId = if (isSelected) null else part.id
                                        }
                                        .border(
                                            width = if (isSelected) 1.5.dp else 0.8.dp,
                                            color = if (isSelected) InkNavy else Color(0xFFCBD5E1),
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) chipColor else chipColor.copy(alpha = 0.45f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "Part ${part.id}",
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = InkNavy,
                                            fontFamily = writingStyle.headingFontFamily
                                        )
                                        Text(
                                            text = "• ${part.tabLabel}",
                                            fontSize = 11.sp,
                                            color = InkCharcoal
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (isPartFullyComplete) Color(0xFF15803D) else InkNavy.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = "$partCompleted/${partTopics.size}",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isPartFullyComplete) Color.White else InkNavy,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Section header with topics count and filter summary
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (selectedPartId == null) "Calculus Curriculum Index" else "Part $selectedPartId Topics",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkNavy,
                                fontFamily = writingStyle.headingFontFamily
                            )
                            val visibleCompleted = filteredTopics.count { completedTopicIds.contains(it.id) }
                            Text(
                                text = "$visibleCompleted of ${filteredTopics.size} studied in this view",
                                fontSize = 11.sp,
                                color = if (visibleCompleted == filteredTopics.size && filteredTopics.isNotEmpty()) Color(0xFF16A34A) else InkCharcoal
                            )
                        }
                        Text(
                            text = "Showing ${filteredTopics.size} of $totalTopics",
                            fontSize = 11.5.sp,
                            color = InkCharcoal
                        )
                    }
                }

                // Empty state if search yield no results
                if (filteredTopics.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "🔍", fontSize = 28.sp)
                                Text(
                                    text = "No topics matched \"$searchQuery\"",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InkNavy
                                )
                                Text(
                                    text = "Try searching for terms like 'slope', 'limit', 'derivative', 'integral', or 'series'.",
                                    fontSize = 12.sp,
                                    color = InkCharcoal
                                )
                                Button(
                                    onClick = {
                                        searchQuery = ""
                                        selectedPartId = null
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
                                ) {
                                    Text("Reset Filters")
                                }
                            }
                        }
                    }
                }

                // --- TOPICS CARDS LIST ---
                items(filteredTopics, key = { it.id }) { topic ->
                    val isCompleted = completedTopicIds.contains(topic.id)
                    val isCurrent = topic.id == currentTopicId
                    val isUnlocked = studyMode || unlockedTopicIds.contains(topic.id)

                    HomeTopicCard(
                        topic = topic,
                        isCompleted = isCompleted,
                        isCurrent = isCurrent,
                        isUnlocked = isUnlocked,
                        searchQuery = searchQuery,
                        onToggleCompleted = onToggleTopicCompleted?.let { callback ->
                            { callback(topic.id) }
                        },
                        onOpenTopic = { onSelectTopic(topic.id) },
                        onOpenQuiz = { onOpenQuiz(topic.partId) }
                    )
                }

                // Bottom spacer for comfortable scroll above navigation bar
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Hero Card representing the handwritten Calculus Notebook cover,
 * progress stats, part-by-part visual tracking meter, and 1-tap "Resume Reading" action.
 */
@Composable
private fun HeroNotebookCoverCard(
    completedCount: Int,
    totalTopics: Int,
    progressFraction: Float,
    currentTopic: Topic,
    studyMode: Boolean,
    onToggleStudyMode: (Boolean) -> Unit,
    completedTopicIds: Set<Int>,
    onSelectPart: (Int) -> Unit,
    onResumeTopic: () -> Unit
) {
    val writingStyle = LocalWritingStyle.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFDE68A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Cover Badge & Milestone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF08A),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFACC15))
                ) {
                    Text(
                        text = "📓 MASTER COURSE NOTEBOOK",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF854D0E),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Completion Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (completedCount > 0) Color(0xFFDCFCE7) else Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(
                        0.8.dp,
                        if (completedCount > 0) Color(0xFF86EFAC) else Color(0xFFCBD5E1)
                    )
                ) {
                    Text(
                        text = "$completedCount / $totalTopics Completed (${(progressFraction * 100).toInt()}%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (completedCount > 0) Color(0xFF166534) else InkCharcoal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // Title & Concept description
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "Intuitive Calculus • Handwritten Notes",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkNavy,
                    fontFamily = writingStyle.headingFontFamily
                )
                Text(
                    text = "Understand the language of the universe through visual diagrams, plain English intuition, interactive formulas, and step-by-step worked solutions.",
                    fontSize = 12.5.sp,
                    color = InkCharcoal,
                    lineHeight = 17.sp
                )
            }

            // Pencil Progress Bar with Hand-sketched Graphite style
            PencilProgressBar(
                currentTopic = completedCount,
                totalTopics = totalTopics,
                compact = false,
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            )

            // Granular Part-by-Part Visual Progress Breakdown
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                    .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curriculum Milestone Progress",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = InkNavy
                    )
                    Text(
                        text = "${totalTopics - completedCount} topics left to master",
                        fontSize = 10.5.sp,
                        color = if (completedCount == totalTopics) Color(0xFF16A34A) else Color(0xFF64748B),
                        fontWeight = if (completedCount == totalTopics) FontWeight.Bold else FontWeight.Normal
                    )
                }

                // 5-Part Progress Mini-Gauges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CalculusCurriculum.parts.forEachIndexed { idx, part ->
                        val partTopics = CalculusCurriculum.topics.filter { it.partId == part.id }
                        val partDone = partTopics.count { completedTopicIds.contains(it.id) }
                        val partFrac = if (partTopics.isNotEmpty()) partDone.toFloat() / partTopics.size else 0f
                        val partDoneAll = partDone == partTopics.size && partTopics.isNotEmpty()
                        val chipColor = partAccentColors[idx % partAccentColors.size]
                        val chipDark = partDarkAccents[idx % partDarkAccents.size]

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onSelectPart(part.id) }
                                .background(if (partDoneAll) Color(0xFFDCFCE7) else chipColor.copy(alpha = 0.35f))
                                .padding(horizontal = 4.dp, vertical = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "P${part.id}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (partDoneAll) Color(0xFF15803D) else chipDark
                            )
                            LinearProgressIndicator(
                                progress = { partFrac },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (partDoneAll) Color(0xFF16A34A) else chipDark,
                                trackColor = Color(0xFFE2E8F0)
                            )
                            Text(
                                text = "$partDone/${partTopics.size}",
                                fontSize = 9.sp,
                                color = InkCharcoal,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Resume Reading Callout Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onResumeTopic() }
                    .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CURRENT LESSON • TOPIC #${currentTopic.id}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D4ED8)
                        )
                        Text(
                            text = currentTopic.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            fontFamily = writingStyle.headingFontFamily,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentTopic.formulaTitle,
                            fontSize = 11.sp,
                            color = InkCharcoal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onResumeTopic,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InkNavy),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "Open 📓",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * An individual topic card showcasing title, part category, plain intuition teaser,
 * rendered math formula, interactive completion status toggle, and direct 1-tap link to open the topic in the notebook.
 */
@Composable
private fun HomeTopicCard(
    topic: Topic,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isUnlocked: Boolean,
    searchQuery: String = "",
    onToggleCompleted: (() -> Unit)? = null,
    onOpenTopic: () -> Unit,
    onOpenQuiz: () -> Unit
) {
    val writingStyle = LocalWritingStyle.current
    val partIndex = (topic.partId - 1).coerceIn(0, partAccentColors.size - 1)
    val partColor = partAccentColors[partIndex]
    val partDarkColor = partDarkAccents[partIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onOpenTopic() }
            .testTag("home_topic_card_${topic.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isCompleted -> Color(0xFFFAFDFA)
                isCurrent -> Color(0xFFF0FDF4)
                else -> Color.White
            }
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrent || isCompleted) 1.5.dp else 1.dp,
            color = when {
                isCompleted -> Color(0xFF86EFAC)
                isCurrent -> Color(0xFF93C5FD)
                else -> Color(0xFFE2E8F0)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Part Badge + Topic Number + Interactive Completion Toggle Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Part Badge
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = partColor,
                        border = androidx.compose.foundation.BorderStroke(0.6.dp, partDarkColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "Part ${topic.partId} • ${topic.partTitle}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = partDarkColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Topic Number Pill
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "#${topic.id.toString().padStart(2, '0')}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkCharcoal,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                // Interactive Status Indicator (Clickable to toggle completion)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        isCompleted -> Color(0xFFDCFCE7)
                        isCurrent -> Color(0xFFDBEAFE)
                        else -> Color(0xFFF8FAFC)
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = when {
                            isCompleted -> Color(0xFF86EFAC)
                            isCurrent -> Color(0xFF93C5FD)
                            else -> Color(0xFFCBD5E1)
                        }
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onToggleCompleted?.invoke() }
                        .testTag("toggle_completion_topic_${topic.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Topic completed - tap to unmark",
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Completed ✓",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                        } else if (isCurrent) {
                            Icon(
                                imageVector = Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Current topic - tap to mark completed",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Current 📖",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D4ED8)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.RadioButtonUnchecked,
                                contentDescription = "Tap to mark completed",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Mark Studied",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            // Topic Title
            Text(
                text = topic.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = InkNavy,
                fontFamily = writingStyle.headingFontFamily
            )

            // Search Match Badges (Term, Equation, Definition, Example, Tip)
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim()
                val matchesFormula = topic.formulaLatex.contains(q, ignoreCase = true) ||
                        topic.formulaTitle.contains(q, ignoreCase = true) ||
                        topic.formulaExplanation.contains(q, ignoreCase = true)
                val matchesDefinition = topic.plainEnglishIntuition.contains(q, ignoreCase = true)
                val matchesExample = topic.exampleProblem.contains(q, ignoreCase = true) ||
                        topic.exampleSteps.any { it.explanation.contains(q, ignoreCase = true) || it.mathExpression.contains(q, ignoreCase = true) } ||
                        topic.practiceProblem.prompt.contains(q, ignoreCase = true)
                val matchesTip = topic.teacherTip.contains(q, ignoreCase = true) ||
                        topic.commonMistake.contains(q, ignoreCase = true)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (matchesFormula) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFEF3C7)) {
                            Text("🧮 Equation Match", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    if (matchesDefinition) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFDCFCE7)) {
                            Text("📖 Definition Match", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D), modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    if (matchesExample) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFFEDD5)) {
                            Text("✏️ Example Match", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9A3412), modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                    if (matchesTip) {
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFFCE7F3)) {
                            Text("💡 Tip Match", fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9D174D), modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                        }
                    }
                }
            }

            // Plain English Intuition Teaser (2 lines max)
            Text(
                text = topic.plainEnglishIntuition,
                fontSize = 12.5.sp,
                color = InkCharcoal,
                lineHeight = 17.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Formula Preview Container
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "FORMULA: ${topic.formulaTitle}",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    LatexMathView(
                        latex = topic.formulaLatex,
                        fontSize = 13.sp,
                        textColor = InkNavy,
                        mathFontFamily = writingStyle.mathFontFamily
                    )
                }
            }

            // Card Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${topic.exampleSteps.size} worked steps • 1 practice challenge",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                // Direct Open Link Button
                TextButton(
                    onClick = onOpenTopic,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text(
                        text = "Open in Notebook 📓 →",
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
