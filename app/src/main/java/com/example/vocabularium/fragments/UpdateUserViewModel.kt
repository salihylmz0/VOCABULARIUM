package com.example.vocabularium.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.LogPrinter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.vocabularium.activities.AuthenticationActivity
import com.example.vocabularium.activities.MainActivity
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.animations.Rotate
import com.example.vocabularium.animations.Slide
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.User
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(@ApplicationContext val context: Context, val firebaseRepo:FirebaseDataRepository) : ViewModel() {
    val userInfo = MutableLiveData<User>()
    val storageReference = FirebaseStorage.getInstance().getReference("Users")
    val toastMessage = MutableLiveData<String>()
    val animation = Rotate()
    val animation2 = Attention()
    val user = Firebase.auth.currentUser
    val auth = Firebase.auth
    val fbDataBaseRef = Firebase.database.reference
    val storageRef = FirebaseStorage.getInstance().getReference("Users/profile_images/${user?.uid}")
    fun getUserInfo(){
        firebaseRepo.getUserInformation(userInfo)
    }

    fun loadImageToFBStorage(uri: Uri) {
        if (user!=null){
            val fileRef = storageReference.child("profile_images").child("${user.uid}").putFile(uri)
                .addOnCompleteListener {
                    toastMessage.value = "Image was successfully loaded"
                }.addOnFailureListener {
                    toastMessage.value = "Something went wrong during loading"
                }
        }

    }
    fun getProfileImage(view: ImageView){
        if (user != null){
            storageRef.downloadUrl.addOnSuccessListener {
                if (it != null) Glide.with(context).load(it).circleCrop().into(view)
            }
        }
    }
    fun applyChanges(name:EditText,email:EditText,dialog: AlertDialog.Builder){
        if (name.text.toString().isNotEmpty() && email.text.toString().isNotEmpty()){
            changeName(name.text.toString())
            changeEmail(email.text.toString(),dialog)
        }else toastMessage.value = "Fields can not be empty!!"
    }

    fun changeName(name:String){
        if (userInfo!=null){
            if(userInfo.value?.userName != name){
                user?.let {
                    fbDataBaseRef.child("User Information").child(user.uid).child("userName")
                        .setValue(name).addOnCompleteListener {
                            if (it.isSuccessful) toastMessage.value = "Name was successfully changed"
                        }
                }
            }
        }
    }
    fun changeEmail(email:String,dialog: AlertDialog.Builder){
        if (userInfo != null){
            user?.let {user->
                if (user.email != email){
                    val credential = EmailAuthProvider.getCredential(user.email!!,userInfo.value?.userPassword!!)
                    user.reauthenticate(credential).addOnCompleteListener {
                        user.verifyBeforeUpdateEmail(email).addOnCompleteListener {
                            if (it.isSuccessful){
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }
    }
    fun deleteAccount(context:Context,button:Button){
        user?.let {
            user.delete().addOnCompleteListener {
                if (it.isSuccessful){
                    animation2.Bounce(button).apply { duration = 1500; start() }
                    auth.signOut()
                    toastMessage.value = "User successfully deleted"
                    val intent = Intent(context,MainActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }
    }
}