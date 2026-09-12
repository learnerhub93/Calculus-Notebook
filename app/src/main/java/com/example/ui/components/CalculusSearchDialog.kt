package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.CalculusCurriculum
import com.example.model.Topic
import com.example.ui.components.math.LatexMathView
import com.example.ui.theme.*

/**
 * Category classification for search matches across calculus notebook content.
 */
enum class SearchMatchCategory(val label: String, val icon: String, val badgeColor: Color, val textColor: Color) {
    ALL("All Results", "🔍", Color(0xFFF1F5F9), Color(0xFF334155)),
    EQUATION("Equations", "🧮", Color(0xFFFEF3C7), Color(0xFF92400E)),
    DEFINITION("Definitions", "📖", Color(0xFFDCFCE7), Color(0xFF15803D)),
    TERM("Terms & Titles", "🏷️", Color(0xFFE0E7FF), Color(0xFF3730A3)),
    EXAMPLE("Examples", "✏️", Color(0xFFFFEDD5), Color(0xFF9A3412)),
    TIP("Tips & Pitfalls", "💡", Color(0xFFFCE7F3), Color(0xFF9D174D))
}

/**
 * Search result entry describing where in the topic a query was matched.
 */
data class TopicSearchResult(
    val topic: Topic,
    val category: SearchMatchCategory,
    val sectionName: String,
    val snippetText: String,
    val highlightTerm: String,
    val mathLatex: String? = null
)

/**
 * Full-screen modal search dialog for finding terms, definitions, formulas, and equations
 * across all 25 chapters of the calculus notebook.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculusSearchDialog(
    onDismiss: () -> Unit,
    onSelectTopic: (Int) -> Unit,
    initialQuery: String = "",
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var selectedCategory by remember { mutableStateOf(SearchMatchCategory.ALL) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Popular and high-yield calculus search suggestions
    val popularQueries = listOf(
        "Chain Rule", "Limits", "dy/dx", "Riemann Sum", "Product Rule",
        "Power Rule", "Integration by Parts", "Taylor Series", "Concavity",
        "Fundamental Theorem", "Tangent Line", "Optimization", "Substitution"
    )

    // Comprehensive search engine across all curriculum topics
    val allResults = remember(searchQuery) {
        val query = searchQuery.trim()
        if (query.isEmpty()) {
            emptyList()
        } else {
            performCalculusSearch(query)
        }
    }

    // Filter results by selected category
    val filteredResults = remember(allResults, selectedCategory) {
        if (selectedCategory == SearchMatchCategory.ALL) {
            allResults
        } else {
            allResults.filter { it.category == selectedCategory }
        }
    }

    // Category match counts
    val categoryCounts = remember(allResults) {
        mapOf(
            SearchMatchCategory.ALL to allResults.size,
            SearchMatchCategory.EQUATION to allResults.count { it.category == SearchMatchCategory.EQUATION },
            SearchMatchCategory.DEFINITION to allResults.count { it.category == SearchMatchCategory.DEFINITION },
            SearchMatchCategory.TERM to allResults.count { it.category == SearchMatchCategory.TERM },
            SearchMatchCategory.EXAMPLE to allResults.count { it.category == SearchMatchCategory.EXAMPLE },
            SearchMatchCategory.TIP to allResults.count { it.category == SearchMatchCategory.TIP }
        )
    }

    // Auto-focus the search field when dialog appears
    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .testTag("calculus_search_dialog"),
            containerColor = PaperCream,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PaperCream)
                        .statusBarsPadding()
                ) {
                    // Header Bar with Search Box and Close Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search terms, formulas, definitions...",
                                    fontSize = 14.sp,
                                    color = InkCharcoal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = InkNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = InkCharcoal,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = InkNavy,
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedTextColor = InkNavy,
                                unfocusedTextColor = InkNavy
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester)
                                .testTag("search_dialog_input")
                        )

                        // Close button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFE2E8F0), CircleShape)
                                .testTag("search_dialog_close")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Search",
                                tint = InkNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Category Filter Chips Row
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(SearchMatchCategory.values()) { category ->
                            val isSelected = selectedCategory == category
                            val count = categoryCounts[category] ?: 0

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                label = {
                                    Text(
                                        text = "${category.icon} ${category.label} ($count)",
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = InkNavy,
                                    selectedLabelColor = Color.White,
                                    containerColor = Color.White,
                                    labelColor = InkNavy
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) InkNavy else Color(0xFFE2E8F0),
                                    selectedBorderColor = InkNavy,
                                    borderWidth = 1.dp
                                ),
                                modifier = Modifier.testTag("search_filter_${category.name.lowercase()}")
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(PaperCream)
            ) {
                if (searchQuery.isBlank()) {
                    // Empty Query State: Search prompt + Popular Calculus Suggestions
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "🔎", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Find Any Calculus Concept",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy
                        )
                        Text(
                            text = "Search equations (e.g. dy/dx, ∫, f'(x)), definitions, or worked steps across all 25 notebook chapters.",
                            fontSize = 12.5.sp,
                            color = InkCharcoal,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Quick suggestion pills
                        Text(
                            text = "POPULAR CALCULUS TERMS & EQUATIONS",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B),
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Flow-style wrapped suggestions using FlowRow or Column/Row
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val rows = popularQueries.chunked(3)
                            rows.forEach { rowItems ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    rowItems.forEach { suggestion ->
                                        SuggestionChip(
                                            onClick = { searchQuery = suggestion },
                                            label = {
                                                Text(
                                                    text = suggestion,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = InkNavy
                                                )
                                            },
                                            shape = RoundedCornerShape(16.dp),
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = Color.White
                                            ),
                                            border = SuggestionChipDefaults.suggestionChipBorder(
                                                enabled = true,
                                                borderColor = Color(0xFFCBD5E1)
                                            ),
                                            modifier = Modifier.testTag("search_suggestion_$suggestion")
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (filteredResults.isEmpty()) {
                    // No Results Found State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "📐", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No matches for \"$searchQuery\"",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedCategory != SearchMatchCategory.ALL)
                                "No results in ${selectedCategory.label}. Try selecting 'All Results' or checking your spelling."
                            else
                                "Try searching for mathematical symbols like 'dx', 'lim', 'int', or topics like 'derivatives', 'tangents', 'integrals'.",
                            fontSize = 12.5.sp,
                            color = InkCharcoal,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        if (selectedCategory != SearchMatchCategory.ALL) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { selectedCategory = SearchMatchCategory.ALL },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = InkNavy)
                            ) {
                                Text("Switch to All Results", fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    // Search Results List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Found ${filteredResults.size} match${if (filteredResults.size == 1) "" else "es"} for \"$searchQuery\"",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkCharcoal,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }

                        items(filteredResults) { result ->
                            SearchResultCard(
                                result = result,
                                onClick = {
                                    onSelectTopic(result.topic.id)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Individual card presenting a calculus match with highlighted snippet and LaTeX equation preview.
 */
@Composable
private fun SearchResultCard(
    result: TopicSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("search_result_topic_${result.topic.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Row: Part Badge + Chapter + Match Category Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = InkNavy.copy(alpha = 0.08f)
                    ) {
                        Text(
                            text = "Part ${result.topic.partId} • Ch ${result.topic.chapterNumber}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = InkNavy,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = result.sectionName,
                        fontSize = 11.sp,
                        color = InkCharcoal,
                        fontStyle = FontStyle.Italic
                    )
                }

                // Match Category Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = result.category.badgeColor,
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, result.category.textColor.copy(alpha = 0.4f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(text = result.category.icon, fontSize = 10.sp)
                        Text(
                            text = result.category.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = result.category.textColor
                        )
                    }
                }
            }

            // Topic Title
            Text(
                text = result.topic.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = InkNavy,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Formatted LaTeX Equation preview if match is an equation or has math
            if (!result.mathLatex.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(0.8.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        if (result.topic.formulaTitle.isNotBlank()) {
                            Text(
                                text = result.topic.formulaTitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = InkNavy.copy(alpha = 0.8f)
                            )
                        }
                        LatexMathView(
                            latex = result.mathLatex,
                            fontSize = 15.sp,
                            textColor = InkNavy
                        )
                    }
                }
            }

            // Highlighted Snippet text showing match context
            if (result.snippetText.isNotBlank()) {
                val highlightedText = buildAnnotatedString {
                    val text = result.snippetText
                    val query = result.highlightTerm.trim()
                    if (query.isEmpty()) {
                        append(text)
                    } else {
                        var currentIndex = 0
                        val lowerText = text.lowercase()
                        val lowerQuery = query.lowercase()

                        while (currentIndex < text.length) {
                            val matchIndex = lowerText.indexOf(lowerQuery, currentIndex)
                            if (matchIndex == -1) {
                                append(text.substring(currentIndex))
                                break
                            }
                            if (matchIndex > currentIndex) {
                                append(text.substring(currentIndex, matchIndex))
                            }
                            withStyle(
                                SpanStyle(
                                    background = Color(0xFFFEF08A),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF854D0E)
                                )
                            ) {
                                append(text.substring(matchIndex, matchIndex + query.length))
                            }
                            currentIndex = matchIndex + query.length
                        }
                    }
                }

                Text(
                    text = highlightedText,
                    fontSize = 12.sp,
                    color = InkNavy.copy(alpha = 0.85f),
                    lineHeight = 17.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Open in Notebook →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkNavy
                )
            }
        }
    }
}

/**
 * Searches across all topic properties for matching terms, definitions, and equations,
 * returning prioritized and formatted search results.
 */
private fun performCalculusSearch(query: String): List<TopicSearchResult> {
    val results = mutableListOf<TopicSearchResult>()
    val queryLower = query.lowercase()

    CalculusCurriculum.topics.forEach { topic ->
        var hasAddedTopic = false

        // 1. Check Title & Short Title (TERM)
        if (topic.title.contains(query, ignoreCase = true) ||
            topic.shortTitle.contains(query, ignoreCase = true)
        ) {
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.TERM,
                    sectionName = "Chapter Title",
                    snippetText = topic.plainEnglishIntuition.take(130) + "...",
                    highlightTerm = query,
                    mathLatex = topic.formulaLatex
                )
            )
            hasAddedTopic = true
        }

        // 2. Check Formula & LaTeX Equation (EQUATION)
        val matchesFormulaLatex = topic.formulaLatex.contains(query, ignoreCase = true)
        val matchesFormulaTitle = topic.formulaTitle.contains(query, ignoreCase = true)
        val matchesFormulaExplanation = topic.formulaExplanation.contains(query, ignoreCase = true)

        if (matchesFormulaLatex || matchesFormulaTitle || matchesFormulaExplanation) {
            val snippet = if (matchesFormulaExplanation) {
                extractSnippet(topic.formulaExplanation, query)
            } else {
                topic.formulaExplanation.take(130)
            }
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.EQUATION,
                    sectionName = "Formula: ${topic.formulaTitle}",
                    snippetText = snippet,
                    highlightTerm = query,
                    mathLatex = topic.formulaLatex
                )
            )
            hasAddedTopic = true
        }

        // 3. Check Plain English Definition & Intuition (DEFINITION)
        if (topic.plainEnglishIntuition.contains(query, ignoreCase = true)) {
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.DEFINITION,
                    sectionName = "Concept Definition & Intuition",
                    snippetText = extractSnippet(topic.plainEnglishIntuition, query),
                    highlightTerm = query,
                    mathLatex = topic.formulaLatex
                )
            )
            hasAddedTopic = true
        }

        // 4. Check Worked Example Steps & Problem (EXAMPLE)
        val exampleProblemMatch = topic.exampleProblem.contains(query, ignoreCase = true)
        val stepMatch = topic.exampleSteps.find {
            it.explanation.contains(query, ignoreCase = true) || it.mathExpression.contains(query, ignoreCase = true)
        }
        val practiceMatch = topic.practiceProblem.prompt.contains(query, ignoreCase = true) ||
                topic.practiceProblem.hint.contains(query, ignoreCase = true)

        if (exampleProblemMatch || stepMatch != null || practiceMatch) {
            val (section, snippet, math) = when {
                stepMatch != null -> Triple(
                    "Worked Step ${stepMatch.stepNumber}",
                    extractSnippet(stepMatch.explanation, query),
                    stepMatch.mathExpression
                )
                exampleProblemMatch -> Triple(
                    "Worked Example Problem",
                    extractSnippet(topic.exampleProblem, query),
                    topic.formulaLatex
                )
                else -> Triple(
                    "Practice Problem",
                    extractSnippet(topic.practiceProblem.prompt, query),
                    null
                )
            }
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.EXAMPLE,
                    sectionName = section,
                    snippetText = snippet,
                    highlightTerm = query,
                    mathLatex = math
                )
            )
            hasAddedTopic = true
        }

        // 5. Check Pitfalls and Tips (TIP)
        if (topic.teacherTip.contains(query, ignoreCase = true)) {
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.TIP,
                    sectionName = "Teacher Tip",
                    snippetText = extractSnippet(topic.teacherTip, query),
                    highlightTerm = query,
                    mathLatex = null
                )
            )
        } else if (topic.commonMistake.contains(query, ignoreCase = true)) {
            results.add(
                TopicSearchResult(
                    topic = topic,
                    category = SearchMatchCategory.TIP,
                    sectionName = "Common Pitfall to Avoid",
                    snippetText = extractSnippet(topic.commonMistake, query),
                    highlightTerm = query,
                    mathLatex = null
                )
            )
        }
    }

    // Sort results: Term matches first, then Equation matches, then Definition matches, etc.
    return results.sortedBy {
        when (it.category) {
            SearchMatchCategory.TERM -> 0
            SearchMatchCategory.EQUATION -> 1
            SearchMatchCategory.DEFINITION -> 2
            SearchMatchCategory.EXAMPLE -> 3
            SearchMatchCategory.TIP -> 4
            else -> 5
        }
    }
}

/**
 * Extracts a readable substring window centered around the query match.
 */
private fun extractSnippet(fullText: String, query: String, radius: Int = 50): String {
    val index = fullText.indexOf(query, ignoreCase = true)
    if (index == -1) return fullText.take(radius * 2)

    val start = (index - radius).coerceAtLeast(0)
    val end = (index + query.length + radius).coerceAtMost(fullText.length)

    val prefix = if (start > 0) "... " else ""
    val suffix = if (end < fullText.length) " ..." else ""

    return prefix + fullText.substring(start, end).trim() + suffix
}
