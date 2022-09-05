package com.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizapp.model.Quiz
import com.quizapp.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuizViewModel(private val quizRepository : QuizRepository):ViewModel() {
    init{
        viewModelScope.launch(Dispatchers.IO) {
            quizRepository.getQuiz()
        }
    }

    val quiz : LiveData<Quiz>
    get() = quizRepository.quiz

    var scoreUp = MutableLiveData<Int>(0)

    fun updateScore(score : Int){
        scoreUp.value = scoreUp.value?.plus(score)
    }
    fun getScore() : MutableLiveData<Int> {
        return scoreUp
    }
}