package com.example.vocabularium.fragments

import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.animations.Slide
import com.example.vocabularium.exceptions.MyWordsSingleton
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.retrofit.DictionaryResponse
import com.example.vocabularium.retrofit.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWordsPracticeViewModel():ViewModel() {
    var practiceWords = MyWordsSingleton.practiceWords
    var position = MutableLiveData<Int>(0)
    var user = Firebase.auth.currentUser
    val reference = Firebase.database.reference
    var animation = Slide()
    val retrofitClient = RetrofitClient
    var apiData = MutableLiveData<DictionaryResponse>()
    var keeper = ArrayList<String>()
    init {
        practiceWords.forEach {
            keeper.add(it.wordState)
        }
    }
    fun forward(view: View){
        position.value?.let {
            if (it < keeper.size.minus(1)){ position.value = position.value?.plus(1) }
        }
        animation.InLeft(view).apply { duration = 500; start()}
    }
    fun back(view: View){
        position.value?.let {
            if (it >= 0){ position.value = position.value?.minus(1) }
        }
        animation.InRight(view).apply { duration = 500; start()}
    }
    fun getWordInfo(word:String){
        retrofitClient.api.getWordInfo(word).enqueue(object:
            Callback<List<DictionaryResponse>> {
            override fun onResponse(call: Call<List<DictionaryResponse>>, response: Response<List<DictionaryResponse>>) {
                if (response.isSuccessful){
                    val wordInfo = response.body()
                    apiData.value  = wordInfo?.get(0)

                }
            }
            override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {
            }
        })
    }
    fun updateLearningStateOnFB(data: FirebaseWords,state:String){
        if (user != null){
            reference.child("MyWords").
            child(user!!.uid).
            child(data.wordId).
            child("wordState").
            setValue(state)
        }
    }
}