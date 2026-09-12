package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.WritingStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("calculus_notebook_prefs", Context.MODE_PRIVATE)

    private val _currentTopicId = MutableStateFlow(prefs.getInt(KEY_CURRENT_TOPIC, 1))
    val currentTopicId: StateFlow<Int> = _currentTopicId.asStateFlow()

    private val _unlockedTopics = MutableStateFlow(loadUnlockedTopics())
    val unlockedTopics: StateFlow<Set<Int>> = _unlockedTopics.asStateFlow()

    private val _completedTopics = MutableStateFlow(loadCompletedTopics())
    val completedTopics: StateFlow<Set<Int>> = _completedTopics.asStateFlow()

    private val _studyMode = MutableStateFlow(prefs.getBoolean(KEY_STUDY_MODE, false))
    val studyMode: StateFlow<Boolean> = _studyMode.asStateFlow()

    private val _writingStyle = MutableStateFlow(loadWritingStyle())
    val writingStyle: StateFlow<WritingStyle> = _writingStyle.asStateFlow()

    private fun loadWritingStyle(): WritingStyle {
        val styleId = prefs.getString(KEY_WRITING_STYLE, WritingStyle.DEFAULT.id)
        return WritingStyle.fromId(styleId)
    }

    private fun loadUnlockedTopics(): Set<Int> {
        val stored = prefs.getStringSet(KEY_UNLOCKED, null)
        return if (stored != null && stored.isNotEmpty()) {
            stored.mapNotNull { it.toIntOrNull() }.toSet()
        } else {
            setOf(1)
        }
    }

    private fun loadCompletedTopics(): Set<Int> {
        val stored = prefs.getStringSet(KEY_COMPLETED, null)
        return stored?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
    }

    fun setCurrentTopic(topicId: Int) {
        _currentTopicId.value = topicId
        prefs.edit().putInt(KEY_CURRENT_TOPIC, topicId).apply()
    }

    fun markTopicCompleted(topicId: Int) {
        val newCompleted = _completedTopics.value + topicId
        _completedTopics.value = newCompleted
        prefs.edit().putStringSet(KEY_COMPLETED, newCompleted.map { it.toString() }.toSet()).apply()

        // Unlock next topic automatically
        val nextTopic = topicId + 1
        if (nextTopic <= 25) {
            val newUnlocked = _unlockedTopics.value + nextTopic
            _unlockedTopics.value = newUnlocked
            prefs.edit().putStringSet(KEY_UNLOCKED, newUnlocked.map { it.toString() }.toSet()).apply()
        }
    }

    fun toggleTopicCompleted(topicId: Int) {
        if (_completedTopics.value.contains(topicId)) {
            val newCompleted = _completedTopics.value - topicId
            _completedTopics.value = newCompleted
            prefs.edit().putStringSet(KEY_COMPLETED, newCompleted.map { it.toString() }.toSet()).apply()
        } else {
            markTopicCompleted(topicId)
        }
    }

    fun setStudyMode(enabled: Boolean) {
        _studyMode.value = enabled
        prefs.edit().putBoolean(KEY_STUDY_MODE, enabled).apply()
    }

    fun resetProgress() {
        _completedTopics.value = emptySet()
        _unlockedTopics.value = setOf(1)
        _currentTopicId.value = 1
        prefs.edit()
            .remove(KEY_COMPLETED)
            .putStringSet(KEY_UNLOCKED, setOf("1"))
            .putInt(KEY_CURRENT_TOPIC, 1)
            .apply()
    }

    fun getQuizScore(partId: Int): Int {
        return prefs.getInt("quiz_score_$partId", -1)
    }

    fun saveQuizScore(partId: Int, score: Int) {
        prefs.edit().putInt("quiz_score_$partId", score).apply()
    }

    fun setWritingStyle(style: WritingStyle) {
        _writingStyle.value = style
        prefs.edit().putString(KEY_WRITING_STYLE, style.id).apply()
    }

    fun getTopicSketch(topicId: Int): String? {
        return prefs.getString("sketch_topic_$topicId", null)
    }

    fun saveTopicSketch(topicId: Int, sketchData: String) {
        prefs.edit().putString("sketch_topic_$topicId", sketchData).apply()
    }

    fun clearTopicSketch(topicId: Int) {
        prefs.edit().remove("sketch_topic_$topicId").apply()
    }

    companion object {
        private const val KEY_CURRENT_TOPIC = "current_topic"
        private const val KEY_UNLOCKED = "unlocked_topics"
        private const val KEY_COMPLETED = "completed_topics"
        private const val KEY_STUDY_MODE = "study_mode"
        private const val KEY_WRITING_STYLE = "writing_style"
    }
}
