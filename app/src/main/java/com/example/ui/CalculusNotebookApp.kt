package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CalculusCurriculum
import com.example.data.ProgressRepository
import com.example.model.PartQuiz
import com.example.model.WritingStyle
import com.example.ui.components.*
import com.example.ui.theme.LocalWritingStyle
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    NOTEBOOK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculusNotebookApp(
    repository: ProgressRepository = ProgressRepository(LocalContext.current)
) {
    val currentTopicId by repository.currentTopicId.collectAsStateWithLifecycle()
    val unlockedTopics by repository.unlockedTopics.collectAsStateWithLifecycle()
    val completedTopics by repository.completedTopics.collectAsStateWithLifecycle()
    val studyMode by repository.studyMode.collectAsStateWithLifecycle()
    val writingStyle by repository.writingStyle.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showTableOfContents by remember { mutableStateOf(false) }
    var showWritingStyleSheet by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var activeQuizPartId by remember { mutableStateOf<Int?>(null) }

    val currentTopic = remember(currentTopicId) {
        CalculusCurriculum.getTopic(currentTopicId)
    }

    val currentPart = remember(currentTopic.partId) {
        CalculusCurriculum.getPart(currentTopic.partId)
    }

    // Scroll state for notebook page content
    val scrollState = rememberScrollState()

    // Reset scroll when topic changes
    LaunchedEffect(currentTopicId) {
        scrollState.scrollTo(0)
    }

    // Colors for the physical notebook sticky index tabs
    val partTabColors = listOf(
        Color(0xFFFEF08A), // Part 1 Yellow
        Color(0xFFBAE6FD), // Part 2 Sky Blue
        Color(0xFFFED7AA), // Part 3 Orange/Peach
        Color(0xFFBBF7D0), // Part 4 Mint Green
        Color(0xFFE9D5FF)  // Part 5 Lavender
    )

    CompositionLocalProvider(LocalWritingStyle provides writingStyle) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                fadeIn(animationSpec = tween(250)) togetherWith fadeOut(animationSpec = tween(250))
            },
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        currentTopicId = currentTopicId,
                        unlockedTopicIds = unlockedTopics,
                        completedTopicIds = completedTopics,
                        studyMode = studyMode,
                        onSelectTopic = { selectedId ->
                            repository.setCurrentTopic(selectedId)
                            currentScreen = AppScreen.NOTEBOOK
                        },
                        onOpenNotebook = {
                            currentScreen = AppScreen.NOTEBOOK
                        },
                        onOpenQuiz = { partId ->
                            activeQuizPartId = partId
                        },
                        onOpenWritingStyles = {
                            showWritingStyleSheet = true
                        },
                        onToggleStudyMode = {
                            repository.setStudyMode(it)
                        },
                        onToggleTopicCompleted = { topicId ->
                            repository.toggleTopicCompleted(topicId)
                        },
                        onOpenSearch = {
                            showSearchDialog = true
                        }
                    )
                }

                AppScreen.NOTEBOOK -> {
                    BackHandler {
                        currentScreen = AppScreen.HOME
                    }

                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        gesturesEnabled = true,
                        drawerContent = {
                            ModalDrawerSheet(
                                drawerContainerColor = PaperCream,
                                drawerContentColor = InkNavy,
                                modifier = Modifier.widthIn(max = 340.dp)
                            ) {
                                CourseSidebarContent(
                                    currentTopicId = currentTopicId,
                                    unlockedTopicIds = unlockedTopics,
                                    completedTopicIds = completedTopics,
                                    studyMode = studyMode,
                                    onToggleStudyMode = { repository.setStudyMode(it) },
                                    onSelectTopic = { selectedId ->
                                        repository.setCurrentTopic(selectedId)
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onOpenQuiz = { partId ->
                                        activeQuizPartId = partId
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onOpenWritingStyles = {
                                        showWritingStyleSheet = true
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onClose = {
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onGoHome = {
                                        currentScreen = AppScreen.HOME
                                        coroutineScope.launch { drawerState.close() }
                                    },
                                    onOpenSearch = {
                                        showSearchDialog = true
                                        coroutineScope.launch { drawerState.close() }
                                    }
                                )
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(PaperCream)
                                        .statusBarsPadding()
                                ) {
                                    // Compact Top Bar (Home Button + Title + Controls)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 6.dp, end = 10.dp, top = 2.dp, bottom = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Back to Home and Title
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { currentScreen = AppScreen.HOME },
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .testTag("notebook_back_to_home")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "Back to Topics Home",
                                                    tint = InkNavy,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            // Title and 'by vivek' Watermark - tap to open learning journey sidebar
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable { coroutineScope.launch { drawerState.open() } }
                                                    .padding(vertical = 2.dp, horizontal = 2.dp)
                                            ) {
                                                Text(
                                                    text = "📓",
                                                    fontSize = 18.sp
                                                )
                                                Column {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                                    ) {
                                                        Text(
                                                            text = "Calculus Notebook",
                                                            fontSize = 15.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = InkNavy,
                                                            fontFamily = writingStyle.headingFontFamily
                                                        )
                                                        Surface(
                                                            shape = RoundedCornerShape(3.dp),
                                                            color = Color(0x121E3A8A),
                                                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0x221E3A8A)),
                                                            modifier = Modifier.testTag("watermark_by_vivek")
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
                                                        text = "Handwritten Course • 25 Chapters",
                                                        fontSize = 10.sp,
                                                        color = InkCharcoal
                                                    )
                                                }
                                            }
                                        }

                                        // Compact Quick Actions
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // Home Button
                                            FilledTonalButton(
                                                onClick = { currentScreen = AppScreen.HOME },
                                                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(28.dp).testTag("notebook_home_btn"),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = Color(0xFFE0E7FF),
                                                    contentColor = InkNavy
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Home,
                                                    contentDescription = "Home",
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Home",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = writingStyle.headingFontFamily
                                                )
                                            }

                                            // Search Button
                                            FilledTonalButton(
                                                onClick = { showSearchDialog = true },
                                                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(28.dp).testTag("notebook_search_btn"),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = Color(0xFFFEF3C7),
                                                    contentColor = Color(0xFF92400E)
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Search,
                                                    contentDescription = "Search",
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Search",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = writingStyle.headingFontFamily
                                                )
                                            }

                                            // Canvas Scratchpad Jump Button
                                            FilledTonalButton(
                                                onClick = {
                                                    coroutineScope.launch {
                                                        scrollState.animateScrollTo(scrollState.maxValue)
                                                    }
                                                },
                                                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(28.dp).testTag("notebook_canvas_btn"),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = Color(0xFFDCFCE7),
                                                    contentColor = Color(0xFF15803D)
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Create,
                                                    contentDescription = "Canvas",
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Sketch",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = writingStyle.headingFontFamily
                                                )
                                            }

                                            // Writing Style / Font Selector button
                                            FilledTonalButton(
                                                onClick = { showWritingStyleSheet = true },
                                                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(28.dp),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = Color(0xFFFEF3C7),
                                                    contentColor = Color(0xFF92400E)
                                                )
                                            ) {
                                                Text(text = writingStyle.icon, fontSize = 11.sp)
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Font",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = writingStyle.headingFontFamily
                                                )
                                            }

                                            // Part Quiz button
                                            IconButton(
                                                onClick = { activeQuizPartId = currentPart.id },
                                                modifier = Modifier
                                                    .background(Color(0xFFFEE2E2), RoundedCornerShape(6.dp))
                                                    .size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Quiz,
                                                    contentDescription = "Take Quiz",
                                                    tint = CorrectionRed,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            // Sidebar / Table of Contents trigger (Opens Learning Journey Sidebar)
                                            FilledTonalButton(
                                                onClick = { coroutineScope.launch { drawerState.open() } },
                                                contentPadding = PaddingValues(horizontal = 7.dp, vertical = 2.dp),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(28.dp),
                                                colors = ButtonDefaults.filledTonalButtonColors(
                                                    containerColor = Color(0xFFE0E7FF),
                                                    contentColor = InkNavy
                                                )
                                            ) {
                                                Icon(Icons.Default.Bookmarks, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Index",
                                                    fontSize = 10.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = writingStyle.headingFontFamily
                                                )
                                            }
                                        }
                                    }

                                    // Compact Sticky Tab Index Row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                            .padding(horizontal = 10.dp, vertical = 1.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        CalculusCurriculum.parts.forEachIndexed { idx, part ->
                                            val isSelected = currentPart.id == part.id
                                            val tabColor = partTabColors[idx % partTabColors.size]

                                            Surface(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                                    .clickable {
                                                        val targetTopicId = part.topicIds.first()
                                                        if (studyMode || unlockedTopics.contains(targetTopicId)) {
                                                            repository.setCurrentTopic(targetTopicId)
                                                        } else {
                                                            coroutineScope.launch { drawerState.open() }
                                                        }
                                                    }
                                                    .border(
                                                        width = if (isSelected) 1.2.dp else 0.8.dp,
                                                        color = if (isSelected) InkNavy else Color(0xFFCBD5E1),
                                                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                                    ),
                                                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                                                color = if (isSelected) tabColor else tabColor.copy(alpha = 0.5f)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = part.tabLabel,
                                                        fontSize = 10.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                        color = InkNavy,
                                                        fontFamily = writingStyle.headingFontFamily
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Compact Animated Pencil Progress Bar
                                    PencilProgressBar(
                                        currentTopic = currentTopic.id,
                                        totalTopics = CalculusCurriculum.topics.size,
                                        compact = true
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
                                // Ruled notebook background with faint blue lines, vertical red margin, and spiral coils
                                NotebookPaperBackground()

                                // Scrollable Topic Content centered in the middle
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .widthIn(max = 680.dp)
                                    ) {
                                        // Topic view with intuition, formula, sketch, example, practice problem, and sticky note
                                        val isCompleted = completedTopics.contains(currentTopic.id)
                                        val hasPrevious = currentTopic.id > 1
                                        val hasNext = currentTopic.id < CalculusCurriculum.topics.size &&
                                                (studyMode || unlockedTopics.contains(currentTopic.id + 1))

                                        // Check if this is the last topic of the current part
                                        val isLastTopicOfPart = currentPart.topicIds.last() == currentTopic.id

                                        TopicContentView(
                                            topic = currentTopic,
                                            isCompleted = isCompleted,
                                            sketchData = repository.getTopicSketch(currentTopic.id),
                                            onSaveSketch = { data ->
                                                repository.saveTopicSketch(currentTopic.id, data)
                                            },
                                            onMarkCompleted = {
                                                repository.markTopicCompleted(currentTopic.id)
                                            },
                                            onPreviousTopic = {
                                                if (hasPrevious) {
                                                    repository.setCurrentTopic(currentTopic.id - 1)
                                                }
                                            },
                                            onNextTopic = {
                                                val nextId = currentTopic.id + 1
                                                if (nextId <= CalculusCurriculum.topics.size) {
                                                    repository.markTopicCompleted(currentTopic.id)
                                                    repository.setCurrentTopic(nextId)
                                                }
                                            },
                                            hasPrevious = hasPrevious,
                                            hasNext = hasNext || currentTopic.id < CalculusCurriculum.topics.size,
                                            onStartQuiz = if (isLastTopicOfPart) {
                                                { activeQuizPartId = currentPart.id }
                                            } else null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

            // Table of Contents Bottom Sheet
            if (showTableOfContents) {
                TableOfContentsSheet(
                    currentTopicId = currentTopicId,
                    unlockedTopicIds = unlockedTopics,
                    completedTopicIds = completedTopics,
                    studyMode = studyMode,
                    onToggleStudyMode = { repository.setStudyMode(it) },
                    onSelectTopic = { selectedId ->
                        repository.setCurrentTopic(selectedId)
                    },
                    onOpenQuiz = { partId ->
                        activeQuizPartId = partId
                    },
                    onOpenWritingStyles = {
                        showWritingStyleSheet = true
                    },
                    onClose = { showTableOfContents = false },
                    onGoHome = {
                        currentScreen = AppScreen.HOME
                        showTableOfContents = false
                    },
                    onOpenSearch = {
                        showTableOfContents = false
                        showSearchDialog = true
                    }
                )
            }

            // Writing Style Font Switcher Bottom Sheet
            if (showWritingStyleSheet) {
                WritingStyleSheet(
                    currentStyle = writingStyle,
                    onSelectStyle = { newStyle ->
                        repository.setWritingStyle(newStyle)
                    },
                    onClose = { showWritingStyleSheet = false }
                )
            }

            // Quiz Dialog
            activeQuizPartId?.let { pId ->
                val quiz = CalculusCurriculum.getQuiz(pId)
                if (quiz != null) {
                    QuizDialog(
                        quiz = quiz,
                        onSaveScore = { score ->
                            repository.saveQuizScore(pId, score)
                        },
                        onDismiss = { activeQuizPartId = null }
                    )
                }
            }

            // Calculus Search Dialog
            if (showSearchDialog) {
                CalculusSearchDialog(
                    onDismiss = { showSearchDialog = false },
                    onSelectTopic = { selectedId ->
                        repository.setCurrentTopic(selectedId)
                        currentScreen = AppScreen.NOTEBOOK
                        showSearchDialog = false
                    }
                )
            }
        }
    }

