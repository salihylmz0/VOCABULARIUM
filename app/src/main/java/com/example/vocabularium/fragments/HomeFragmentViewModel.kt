package com.example.vocabularium.fragments

import android.content.Context
import android.util.Log
import android.view.View
import android.view.animation.Transformation
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vocabularium.R
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.models.LanguageLevels
import com.example.vocabularium.repositories.DataRepository
import com.example.vocabularium.repositories.FirebaseDataRepository
import com.example.vocabularium.repositories.RoomDataRepository
import com.example.vocabularium.room_database.RoomWords
import com.google.android.material.transformation.FabTransformationScrimBehavior
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor( @ApplicationContext val context: Context,
                                                 val roomDataRepository: RoomDataRepository,
                                                 val dataRepository: DataRepository,
                                                 val firebaseDataRepository: FirebaseDataRepository): ViewModel() {
    var allFirebaseWords = MutableLiveData<ArrayList<FirebaseWords>>()
    var levelFirebaseWords=MutableLiveData<ArrayList<FirebaseWords>>()
    var allRoomWords: LiveData<List<RoomWords>>
    val searchedRoomWords = MutableLiveData<List<RoomWords>>()
    var levelRoomWords :LiveData<List<RoomWords>>
    var levelValue = MutableLiveData<String>().also { it.value = "A1" }
    val levelWordsAppData = MutableLiveData<List<List<AppWords>>>()
    var levelsDataHFVM = ArrayList<LanguageLevels>()
    var user = MutableLiveData<FirebaseUser?>()
    var auth = Firebase.auth
    val fbReference = Firebase.database.reference
    var toastMessage = MutableLiveData<String>()
    var isFirstLoggedIn = MutableLiveData<Boolean>(false)
    val searchedDataobserver = Observer<List<RoomWords>>{wordData->
        searchedRoomWords.value = wordData
    }

   val levelFBObserver = Observer<ArrayList<FirebaseWords>>{wordData->
       convertDataFB(wordData,levelWordsAppData)
   }
    val levelRoomObserver = Observer<List<RoomWords>>{wordData->
        convertDataRoom(wordData,levelWordsAppData)
    }

    init {
        user.value = auth.currentUser
        levelsDataHFVM = dataRepository.getLevelsData()
        allRoomWords = roomDataRepository.roomGetAllWords()
        levelRoomWords = roomDataRepository.roomGetLevelWords(levelValue.value.toString())
        getLevelAppData()
    }

    fun getLevelAppData(){
        if (user!=null){
            getLevelWordsFromFB()
        }else getLevelWordsFromRoom()
    }

    //Room CRUD
    fun getLevelWordsFromRoom(){
        levelRoomWords = roomDataRepository.roomGetLevelWords(levelValue.value.toString())
        levelRoomWords.observeForever(levelRoomObserver)
    }
    fun addDataToAppRoom(){
        viewModelScope.launch { roomDataRepository.addDataToApp() }
    }
    fun addWordToRoom(word: RoomWords){
        viewModelScope.launch { roomDataRepository.roomAddWord(word) }
    }
    fun deleteAllWordsFromRoom(){
        viewModelScope.launch { roomDataRepository.roomDeleteAllWords() }
    }
    fun deleteWordWithIdFromRoom(wordId: Int){
        viewModelScope.launch { roomDataRepository.roomDeleteWord(wordId) }
    }
    fun deleteWordWithNameFromRoom(wordName: String){
        viewModelScope.launch { roomDataRepository.roomDeleteWordWithName(wordName) }
    }
    fun searchedWordFromRoom(word: String){
        roomDataRepository.roomSearchedWords(word).observeForever(searchedDataobserver)
    }
    fun removeObservation(observer: Observer<List<RoomWords>>){
        searchedRoomWords.removeObserver(observer)
    }
    fun removeObservation2(observer:Observer<ArrayList<FirebaseWords>>){
        levelFirebaseWords.removeObserver(observer)
    }

    //Firebase CRUD
    suspend fun addDataToFB(){
            if (user.value != null){
                for (i in 0..allRoomWords.value!!.size-1){
                    var data = allRoomWords.value!![i]
                    CoroutineScope(Dispatchers.Main).launch {
                        addWordToFB(FirebaseWords(data.wordId.toString(),data.wordLevel,data.wordName,data.wordState)) }
                }
            }
    }

    fun getAllWordsFromFB():List<AppWords>{
        val allData = ArrayList<AppWords>()
        firebaseDataRepository.getAllWordsFromFb(allFirebaseWords)
        return allData
    }
    fun getLevelWordsFromFB(){
        firebaseDataRepository.getLevelWordsFromFb(levelValue.value!!,levelFirebaseWords)
        levelFirebaseWords.observeForever(levelFBObserver)
    }

    fun deleteAWordFromFB(word: FirebaseWords){
        firebaseDataRepository.deleteAWordFromFb(word)
    }
    fun deleteAllWordsFromFB(){
        firebaseDataRepository.deleteAllWordsFromFB()
    }
    fun convertDataFB(dataToConvert: ArrayList<FirebaseWords>,
                    typeToConvert:MutableLiveData<List<List<AppWords>>>){
        val allData = ArrayList<AppWords>()
        val levelData: List<List<AppWords>>
        dataToConvert.forEach{data->
            allData.add(AppWords(data.wordId.toInt(),data.wordName,data.wordLevel,data.wordState.toBoolean()))
        }
        levelData = allData.chunked(context.resources.getInteger(R.integer.sizeOfStage))
        typeToConvert.value=levelData
    }
    fun convertDataRoom(dataToConvert: List<RoomWords>,
                      typeToConvert:MutableLiveData<List<List<AppWords>>>){
        val allData = ArrayList<AppWords>()
        val levelData: List<List<AppWords>>
        dataToConvert.forEach{data->
            allData.add(AppWords(data.wordId,data.wordName,data.wordLevel,data.wordState.toBoolean()))
        }
        levelData = allData.chunked(context.resources.getInteger(R.integer.sizeOfStage))
        typeToConvert.value=levelData
    }

    //-------------
    fun checkFirstLogin(progressBar: ProgressBar){
        if (user.value != null){
            fbReference.child("First time Login").child(user.value!!.uid).get().addOnSuccessListener{snapshot->
                if (snapshot.value == "false"){
                    toastMessage.value = "First loadings are being made, this may take to 2 minutes, please wait..."
                    isFirstLoggedIn.value = false
                    progressBar.visibility = View.VISIBLE
                    fbReference.child("First time Login").child(user.value!!.uid).setValue("true")
                    CoroutineScope(Dispatchers.Main).launch {
                        addDataToFB()
                    }
                }
            }
        }
    }
    fun updateUserEmail(email:String){
        user.value?.let {user->fbReference.child("User Information").child(user.uid)
            .child("userEmail").setValue(email)}
    }
    suspend fun addWordToFB(word: FirebaseWords){
            user.value?.let {user->
                fbReference.child("Users")
                    .child(user.uid)
                    .child(word.wordLevel)
                    .child(word.wordId)
                    .setValue(word)
            }
    }
}