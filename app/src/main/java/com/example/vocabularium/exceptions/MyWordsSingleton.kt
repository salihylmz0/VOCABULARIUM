package com.example.vocabularium.exceptions

import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords

object MyWordsSingleton {
    var myWord:FirebaseWords = FirebaseWords()
    var myWordFilter:String = ""
    var practiceWords = ArrayList<FirebaseWords>()
}