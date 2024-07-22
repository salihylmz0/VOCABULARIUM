package com.example.vocabularium.fragments

import android.R
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.User
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.example.vocabularium.repositories.RoomDataRepository
import com.example.vocabularium.room_database.RoomWords
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthRegisterFragmentViewModel @Inject constructor(val roomDataRepository:RoomDataRepository,
                                                        val firebaseDataRepository:FirebaseDataRepository) : ViewModel() {
    var auth:FirebaseAuth = FirebaseAuth.getInstance()
    var user = MutableLiveData<FirebaseUser?>()
    var toastMessage = MutableLiveData<String>()
    var fbReference = Firebase.database.reference
    var allRoomWords: LiveData<List<RoomWords>>

    init {
        user.value = auth.currentUser
        allRoomWords = roomDataRepository.roomGetAllWords()
    }

    fun signUpNewUser(name: EditText, email: EditText, password: EditText, passwordConfirmation: EditText){
        if (name.text.toString().isNotEmpty() && email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() &&
            passwordConfirmation.text.toString().isNotEmpty()){
            if (password.text.toString().equals(passwordConfirmation.text.toString())){
                auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{task->
                    if(task.isSuccessful){
                        toastMessage.value = "Signing up is succesfull"
                        user.value = auth.currentUser
                        sendEmailVerification(User(name.text.toString(),email.text.toString(),password.text.toString()))
                        firstLogin()
                    }
                    else{
                        toastMessage.value = "Signing up is failed"
                    }
                }
            }else toastMessage.value = "Passwords are unmatched"
        }else toastMessage.value = "Please fill in the blanks"
    }

    fun addUserInformationToFB(userInfo:User){
        fbReference.child("User Information").
        child(user.value!!.uid).
        setValue(userInfo).addOnCompleteListener {
            auth.signOut()
        }

    }
    fun firstLogin(){
        if (user.value != null){
            fbReference.child("First time Login").child(user.value!!.uid).setValue("false")
        }
    }

    fun sendEmailVerification(userInfo: User) {

        if(user.value != null){
            user.value?.sendEmailVerification()?.addOnCompleteListener{
                if (it.isSuccessful) {toastMessage.value = "Verification mail was sent, Please to get access, verify the mail"
                    addUserInformationToFB(userInfo)
                }else toastMessage.value = "Sending mail failed!"
            }
        }

    }


}