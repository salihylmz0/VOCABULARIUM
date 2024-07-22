package com.example.vocabularium.models

class AppWords(val wordId:Int=0,val wordName:String="",val wordLevel:String="",var wordState:Boolean=false) {
    //in here ı gave an initial values because while reading from firebase system gives a no constructor error. to overcome that problem we have
    //to give inital values to the class that we gonna make our reading into
}