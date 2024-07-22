package com.example.vocabularium.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val baseURL = "https://api.dictionaryapi.dev/"

    val api:DictionaryDao by lazy {
        Retrofit.Builder().baseUrl(baseURL).
        addConverterFactory(GsonConverterFactory.create()).build().create(DictionaryDao::class.java)
    }
}