package com.example.vocabularium.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DictionaryResponse(
    @SerializedName("word")
    @Expose
    val word:String,
    @SerializedName("meanings")
    @Expose
    val meanings:List<Meanings>,
    @SerializedName("phonetics")
    @Expose
    val phonetics:List<Phonetics>){}

data class Phonetics(
    @SerializedName("audio")
    @Expose
    val audio:String
)
data class Meanings(
    @SerializedName("partOfSpeech")
    @Expose
    val typeOfWord:String,
    @SerializedName("definitions")
    @Expose
    val definitions:List<Definition>){}
data class Definition(
    @SerializedName("definition")
    @Expose
    val definition:String,
    @SerializedName("example")
    @Expose
    val example:String){}