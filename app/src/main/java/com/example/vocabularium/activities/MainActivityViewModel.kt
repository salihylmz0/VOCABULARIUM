package com.example.vocabularium.activities

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.vocabularium.models.User
import com.example.vocabularium.repositories.DataRepository
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(@ApplicationContext val context: Context, val firebaseRepo: FirebaseDataRepository): ViewModel(){
    val user = Firebase.auth.currentUser
    val storageRef = FirebaseStorage.getInstance().getReference("Users/profile_images/${user?.uid}")
    val userInfo = MutableLiveData<User>()
    fun getUserInfo(){
        firebaseRepo.getUserInformation(userInfo)
    }

    fun geProfileImageUri(view: ImageView){
        if (user != null){
            storageRef.downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).circleCrop().into(view)
            }
        }
    }
}