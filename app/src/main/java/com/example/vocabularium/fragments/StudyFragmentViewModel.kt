package com.example.vocabularium.fragments

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.animations.Slide
import com.example.vocabularium.exceptions.MySingleton
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.example.vocabularium.repositories.RoomDataRepository
import com.example.vocabularium.retrofit.DictionaryResponse
import com.example.vocabularium.retrofit.RetrofitClient
import com.example.vocabularium.room_database.RoomWords
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StudyFragmentViewModel @Inject constructor(val roomDataRepo: RoomDataRepository, val firebaseDataRep: FirebaseDataRepository) : ViewModel() {
    var user = Firebase.auth.currentUser
    val reference = Firebase.database.reference
    var animation = Slide()
    var studyingData = MutableLiveData<List<AppWords>>()
    val dataKeeper = ArrayList<AppWords>()
    var mySingleton = MySingleton
    var position = MutableLiveData<Int>(0)
    val retrofitClient = RetrofitClient
    var apiData = MutableLiveData<DictionaryResponse>()
    var toastMessage = MutableLiveData<String>()
    init {
        studyingData.value = mySingleton.dataList
        mySingleton.dataList?.forEach { dataKeeper.add(it) }//giving singleton data to temporary value to keep the changes until complete the stage or exit the stage
    }

    //Retrofit api call
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
    fun updateKeeperValue(posit: Int, value: Boolean){
        dataKeeper.get(posit).wordState = value
    }
    fun updateLearningStateOnFB(data:AppWords){
        if (user != null){
            reference.child("Users").
            child(user!!.uid).
            child(data.wordLevel).
            child(data.wordId.toString()).
            child("wordState").
            setValue(data.wordState.toString())
        }
    }
    fun updateLearningStateOnRoom(words:ArrayList<AppWords>){
            words.forEach { roomDataRepo.roomUpdateWord(RoomWords(it.wordId,it.wordName,it.wordState.toString(),it.wordLevel)) }
    }

    fun addMyWordToFB(data: AppWords){
        firebaseDataRep.addMyWordToFB(data, toastMessage)
    }
    fun goForward(view:View){
        position.value = position.value?.plus(1)
        animation.InLeft(view).apply { duration = 500; start()}
    }
    fun goBack(view:View){
        position.value = position.value?.minus(1)
        animation.InRight(view).apply { duration = 500;start() }
    }
    fun updateData(wordName:TextView){
        wordName.text = mySingleton.dataList?.get(position.value!!)?.wordName
    }


}