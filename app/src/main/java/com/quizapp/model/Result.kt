package com.quizapp.model

data class Result(
    val questions: List<Question>,
    val timeInMinutes: Int
)