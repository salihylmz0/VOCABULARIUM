package com.example.vocabularium.animations


import android.animation.AnimatorSet
import android.content.Context
import android.view.animation.AccelerateInterpolator

class Render (var cx: Context){
    var du: Long = 1000

    lateinit var animatorSet: AnimatorSet

    fun setAnimation (animatorSet: AnimatorSet){
        this.animatorSet = animatorSet
    }

    fun setDuration (duration: Long){
        this.du = duration
    }

    fun start(){
        animatorSet.duration = du
        animatorSet.interpolator = AccelerateInterpolator()
        animatorSet.start()
    }









}