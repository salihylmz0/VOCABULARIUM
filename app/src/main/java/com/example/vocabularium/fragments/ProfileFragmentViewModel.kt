package com.example.vocabularium.fragments

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.vocabularium.animations.Rotate
import com.example.vocabularium.animations.Slide
import com.example.vocabularium.animations.Zoom
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.User
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(@ApplicationContext val context: Context, val firebaseRepo:FirebaseDataRepository) : ViewModel() {
    val userInfo = MutableLiveData<User>()
    val userData = MutableLiveData<ArrayList<FirebaseWords>>()
    val learntWordsNumber = MutableLiveData<Int>()
    val progressValue = MutableLiveData<Double>()
    val animation = Rotate()
    val animation2 = Zoom()
    val user = Firebase.auth.currentUser
    val storageRef = FirebaseStorage.getInstance().getReference("Users/profile_images/${user?.uid}")

    fun getUserInfo(){
        firebaseRepo.getUserInformation(userInfo)

    }
    fun getUserProgress(){
        firebaseRepo.getAllWordsFromFb(userData)

        val userDataObserver = Observer<ArrayList<FirebaseWords>>{userData->
            val temporary = userData.filter {item-> item.wordState == "true" }
            learntWordsNumber.value = temporary.size
        }
        userData.observeForever(userDataObserver)
        //__________________________________________
        val learntWordsObserver = Observer<Int>{
            progressValue.value =  (it.toDouble()/userData.value!!.size)*100
        }
        learntWordsNumber.observeForever(learntWordsObserver)
    }
    fun animate(upView: View, lowView:View,image:View){
        animation.InDownLeft(upView).apply {
            duration = 2000
            start()
        }
        animation.In(lowView).apply {
            duration = 2000
            start()
        }
        animation2.In(image).apply { duration = 4000; start() }
    }

    fun geProfileImage(view: ImageView){
        if (user != null){
            storageRef.downloadUrl.addOnSuccessListener {
                if (it != null) Glide.with(context).load(it).circleCrop().into(view)

            }
        }
    }
}