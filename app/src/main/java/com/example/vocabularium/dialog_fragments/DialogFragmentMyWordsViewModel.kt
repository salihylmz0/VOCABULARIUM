package com.example.vocabularium.dialog_fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.retrofit.DictionaryResponse
import com.example.vocabularium.retrofit.RetrofitClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogFragmentMyWordsViewModel():ViewModel() {
    val retrofitClient = RetrofitClient
    var apiData = MutableLiveData<DictionaryResponse>()
    var user = Firebase.auth.currentUser
    val reference = Firebase.database.reference
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
        if (user != null){
            reference.child("Users").
            child(user!!.uid).
            child(data.wordLevel).
            child(data.wordId).
            child("wordState").
            setValue(state)
        }
    }
    fun deleteAWordFromFb(word: FirebaseWords){
        if(user !=null){
            reference.child("MyWords")
                .child(user!!.uid)
                .child(word.wordId).removeValue()
        }
    }
}