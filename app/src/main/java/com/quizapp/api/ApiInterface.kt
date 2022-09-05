package com.quizapp.api


import retrofit2.Response
import retrofit2.http.GET
import com.quizapp.model.Quiz as Quiz

interface ApiInterface {
    @GET("?quiz=true")
    suspend fun getQuiz() : Response<Quiz>

}