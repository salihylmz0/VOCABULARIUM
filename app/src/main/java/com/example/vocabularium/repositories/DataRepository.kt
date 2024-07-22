package com.example.vocabularium.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.vocabularium.dummy_data.DummyData
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.LanguageLevels
import com.example.vocabularium.room_database.RoomDatabaseWords
import com.example.vocabularium.room_database.RoomWords
import com.example.vocabularium.room_database.WordsData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataRepository @Inject constructor(val dummyData: DummyData) {
    private val levelsDataDR = dummyData.getLevelValuesDD()
    fun getLevelsData(): ArrayList<LanguageLevels>{
        return levelsDataDR
    }

}