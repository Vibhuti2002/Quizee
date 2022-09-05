package com.quizapp.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiUtilities {
    val BASE_URL = "https://b4e7d359-c58f-4aa3-a314-726b3baa3852.mock.pstmn.io/"

     fun getInstance() : Retrofit{
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}