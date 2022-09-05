package com.quizapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.quizapp.api.ApiInterface
import com.quizapp.model.Quiz

class QuizRepository(private val apiInterface : ApiInterface) {
    private val quizLiveData = MutableLiveData<Quiz>()

    val quiz : LiveData<Quiz>
    get() = quizLiveData
    suspend fun getQuiz() {
        val result = apiInterface.getQuiz()
        if (result.body() != null)
            quizLiveData.postValue(result.body())
    }
}