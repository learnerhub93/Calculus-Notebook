package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CalculusCurriculum
import com.example.model.Topic
import com.example.ui.theme.LocalWritingStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableOfContentsSheet(
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
    onOpenSearch: (() -> Unit)? = null
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = PaperCream,
        dragHandle = {
            BottomSheetDefaults.DragHandle(color = PencilLead)
        }
    ) {
        CourseSidebarContent(
            currentTopicId = currentTopicId,
            unlockedTopicIds = unlockedTopicIds,
            completedTopicIds = completedTopicIds,
            studyMode = studyMode,
            onToggleStudyMode = onToggleStudyMode,
            onSelectTopic = onSelectTopic,
            onOpenQuiz = onOpenQuiz,
            onOpenWritingStyles = onOpenWritingStyles,
            onClose = onClose,
            onGoHome = onGoHome,
            onOpenSearch = onOpenSearch,
            modifier = Modifier.fillMaxHeight(0.85f)
        )
    }
}
