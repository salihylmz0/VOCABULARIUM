package com.example.vocabularium.fragments

import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.vocabularium.R
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
import javax.inject.Inject

@HiltViewModel
class AuthLoginFragmentViewModel @Inject constructor(val roomDataRepository: RoomDataRepository,val firebaseDataRepository: FirebaseDataRepository) : ViewModel() {
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var toastMessage = MutableLiveData<String>()
    var user = MutableLiveData<FirebaseUser?>()
    var isLoginSuccessful = MutableLiveData<Boolean>(false)
    var fbReference = Firebase.database.reference
    var allRoomWords: LiveData<List<RoomWords>>
    val control = MutableLiveData<Int>(0)
    val isNotEmailVerified = MutableLiveData<Boolean>(false)
    init {
        allRoomWords = roomDataRepository.roomGetAllWords()
    }

    fun login(email:EditText,password:EditText){
        if (email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()){
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    user.value = auth.currentUser
                    if((user.value != null) && user?.value!!.isEmailVerified){
                        toastMessage.value = "Login is succesful"
                        isLoginSuccessful.value = true
                        updateData(password.text.toString())

                    }else{
                        isNotEmailVerified.value = true
                    }
                }else{
                    toastMessage.value ="Login failed, something went wrong, try again."
                }
            }
        }else toastMessage.value = "Fill in the blanks!"
    }

    fun getToRegisterFragment(view:TextView){
        Navigation.findNavController(view).navigate(R.id.action_authLoginFragment_to_authRegisterFragment)
    }

    fun updateData(email:String){
        user.value?.let {user->
            fbReference.child("User Information").child(user.uid).child("userPassword").setValue(email)
        }
    }


}


