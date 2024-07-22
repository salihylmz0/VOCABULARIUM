package com.example.vocabularium.retrofit


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryDao {
    @GET("api/v2/entries/en/{word}")
    fun getWordInfo(@Path("word") word:String): Call<List<DictionaryResponse>>
}