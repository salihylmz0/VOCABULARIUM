package com.example.vocabularium.activities

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.repositories.DataRepository
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.example.vocabularium.repositories.RoomDataRepository
import com.example.vocabularium.room_database.RoomWords
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(@ApplicationContext val context: Context,
                              val roomDataRepository: RoomDataRepository):ViewModel() {
    var allRoomWords: LiveData<List<RoomWords>>
    val user = Firebase.auth.currentUser
    val fbReference = Firebase.database.reference
    val toastMessage = MutableLiveData<String>()
    var isFirstLoggedIn = MutableLiveData<Boolean>(false)
    init {
        allRoomWords = roomDataRepository.roomGetAllWords()
    }
    fun checkFirstLogin(){
        if (user != null){
            fbReference.child("First time Login").child(user.uid).get().addOnSuccessListener{snapshot->
                if (snapshot.value == "false"){
                    toastMessage.value = "First loadings are being made, this may take to 2 minutes, please wait..."
                    isFirstLoggedIn.value = false
                    fbReference.child("First time Login").child(user.uid).setValue("true")
                    CoroutineScope(Dispatchers.Main).launch {
                        addDataToFB()
                    }
                }
            }
        }
    }
    suspend fun addWordToFB(word: FirebaseWords){
        user?.let {user->
            fbReference.child("Users")
                .child(user.uid)
                .child(word.wordLevel)
                .child(word.wordId)
                .setValue(word)
        }
    }
    suspend fun addDataToFB(){
        if (user!= null){
            for (i in 0..allRoomWords.value!!.size-1){
                var data = allRoomWords.value!![i]
                CoroutineScope(Dispatchers.Main).launch {
                    addWordToFB(FirebaseWords(data.wordId.toString(),data.wordLevel,data.wordName,data.wordState)) }
            }
        }
    }
}