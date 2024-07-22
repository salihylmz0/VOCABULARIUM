package com.example.vocabularium

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface Fly{
    fun fly()
}

class Bird():Fly {
    override fun fly() {
        print("bird can fly")
    }
}
fun main(){
    fun isFly(x: Fly){
       x.fly()
    }
    val bird=Bird()
    isFly(bird)
}