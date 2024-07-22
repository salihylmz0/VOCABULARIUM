package com.example.vocabularium.dialog_fragments

import android.app.AlertDialog
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vocabularium.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel@Inject constructor():ViewModel() {
    val fbReference = Firebase.database.reference
    val user = Firebase.auth.currentUser
    val userValue = MutableLiveData<User>()
    var toastMessage = MutableLiveData<String>()
    fun getUserInfo(){
        user?.let {user->fbReference.child("User Information").child(user.uid).get().addOnSuccessListener {dataSnapshot->
            var userInfo = User()
            dataSnapshot?.let {snapshot-> userInfo = snapshot.getValue(User::class.java)!! }
            userValue.value = userInfo }
        }
    }
    fun applyPasswordChanges(currentPassword:TextInputEditText,newPassword:TextInputEditText,confirmPassword:TextInputEditText){
        if (currentPassword.text?.isNotEmpty() ?: false && newPassword.text?.isNotEmpty()?: false && confirmPassword.text?.isNotEmpty()?: false ){
            if (newPassword.text.toString() == confirmPassword.text.toString()){
                changePassword(newPassword.text.toString())
            }else toastMessage.value = "Passwords do not match please control them"
        }else toastMessage.value = "Please fill all fields"
    }
    fun changePassword(password:String){
        if (userValue.value != null){
            user?.let {user->
                user.updatePassword(password).addOnCompleteListener {
                    if (it.isSuccessful){
                        updatePasswordInUser(password)
                    }
                }
            }
        }
    }
    fun updatePasswordInUser(newPassword:String){
        user?.let {user->
            fbReference.child("User Information").child(user.uid).child("userPassword").
            setValue(newPassword).addOnCompleteListener {
                if (it.isSuccessful){
                    toastMessage.value = "Password was successfully changed"
                }else{
                    toastMessage.value = "şifre değiştirme başarısız ${it.exception?.message}"
                    Log.e("hata nedir","${it.exception?.message}")
                }
            }
        }
    }
}