package com.example.vocabularium.dialog_fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class ForgetPasswordDialogViewModel():ViewModel() {
    var toastMessage = MutableLiveData<String>()
    val fbReference = Firebase.database.reference
    val user = Firebase.auth.currentUser
    fun resetEmail(email:String){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastMessage.value = "Password reset email was sent , please check your mail box"
                }
            }
    }
}