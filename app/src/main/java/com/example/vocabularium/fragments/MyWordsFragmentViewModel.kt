package com.example.vocabularium.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyWordsFragmentViewModel @Inject constructor(val firebaseDataRepo:FirebaseDataRepository) : ViewModel() {
    val myWordsFB = MutableLiveData<ArrayList<FirebaseWords>>()
    val fbReference = Firebase.database.reference
    val user = Firebase.auth.currentUser
    val searchedWords = MutableLiveData<ArrayList<FirebaseWords>>()


    fun getMyWordsFromFB(){
        firebaseDataRepo.getMyWordsFromFB(myWordsFB)
    }
    fun searchWords(word:String){
       firebaseDataRepo.getMyWordsFromFB(myWordsFB)
        val myWordsObserver = Observer<ArrayList<FirebaseWords>>{myWords->
            searchedWords.value = myWords.filter { it.wordName == word || it.wordName.contains(word) } as ArrayList
        }
        myWordsFB.observeForever(myWordsObserver)
    }
    fun cleanMyWords(){
        myWordsFB.value?.forEach {
            if(user !=null){
                fbReference.child("MyWords")
                    .child(user!!.uid)
                    .child(it.wordId).removeValue()
            }
        }

    }
}