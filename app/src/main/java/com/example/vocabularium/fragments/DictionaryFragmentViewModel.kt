package com.example.vocabularium.fragments

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.retrofit.DictionaryResponse
import com.example.vocabularium.retrofit.RetrofitClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DictionaryFragmentViewModel : ViewModel() {
    val user = Firebase.auth.currentUser
    val fbReference = Firebase.database.reference
    val retrofitClient = RetrofitClient
    val apiData = MutableLiveData<DictionaryResponse>()
    var toastMessage = MutableLiveData<String?>()
    fun getWordInfo(word:String){
        retrofitClient.api.getWordInfo(word).enqueue(object:
            Callback<List<DictionaryResponse>> {
            override fun onResponse(call: Call<List<DictionaryResponse>>, response: Response<List<DictionaryResponse>>) {
                if (response.isSuccessful){
                    val wordInfo = response.body()
                    apiData.value  = wordInfo?.get(0)
                }else toastMessage.value = "Word was not found"
            }
            override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {
            }
        })
    }
    fun addMyWordToFB(data: FirebaseWords){
        if(user!=null){
            fbReference.child("MyWords").child(user!!.uid).child(data.wordId).setValue(data).addOnCompleteListener {
                toastMessage.value = "This word was added to My Words..."
            }
        }
    }
    fun clearMessage(){
        toastMessage.value = ""
    }
}