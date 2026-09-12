package com.example.model

enum class DiagramType {
    FUNCTION_MACHINE,
    RATE_OF_CHANGE_GRAPH,
    SLOPE_TRIANGLE,
    CURVE_VS_LINE,
    LIMIT_APPROACH,
    LIMIT_HOLE_GRAPH,
    LIMIT_LAWS_CHART,
    CONTINUITY_TEST,
    INSTANTANEOUS_SPEED,
    FIRST_PRINCIPLES_LIMIT,
    INTERACTIVE_TANGENT,
    POWER_RULE_VISUAL,
    PRODUCT_CHAIN_RULE,
    TRIG_EXP_SKETCH,
    LADDER_PROBLEM,
    AREA_PROBLEM,
    INTERACTIVE_RIEMANN_SUM,
    DEFINITE_INTEGRAL_SUM,
    FTC_INVERSE_MACHINE,
    SUBSTITUTION_PUZZLE,
    AREA_BETWEEN_CURVES,
    SERIES_DOMINOES,
    TAYLOR_SERIES_FIT,
    PARTIAL_DERIVATIVE_HILL,
    DIFF_EQ_SLOPE_FIELD
}

data class WorkedStep(
    val stepNumber: Int,
    val explanation: String,
    val mathExpression: String
)

data class PracticeProblem(
    val prompt: String,
    val hint: String,
    val solutionSteps: List<String>,
    val finalAnswer: String
)

data class Topic(
    val id: Int,
    val partId: Int,
    val partTitle: String,
    val chapterNumber: Int,
    val title: String,
    val shortTitle: String,
    val plainEnglishIntuition: String,
    val formulaTitle: String,
    val formulaLatex: String,
    val formulaExplanation: String,
    val diagramType: DiagramType,
    val diagramCaption: String,
    val exampleTitle: String,
    val exampleProblem: String,
    val exampleSteps: List<WorkedStep>,
    val practiceProblem: PracticeProblem,
    val commonMistake: String,
    val teacherTip: String
)

data class Part(
    val id: Int,
    val title: String,
    val subtitle: String,
    val tabLabel: String,
    val topicIds: List<Int>
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class PartQuiz(
    val partId: Int,
    val partTitle: String,
    val questions: List<QuizQuestion>
)
