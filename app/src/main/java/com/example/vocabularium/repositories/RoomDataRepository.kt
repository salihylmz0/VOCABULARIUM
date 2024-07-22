package com.example.vocabularium.repositories
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.util.foreignKeyCheck
import com.example.vocabularium.R
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.room_database.RoomDatabaseWords
import com.example.vocabularium.room_database.RoomWords
import com.example.vocabularium.room_database.WordsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomDataRepository@Inject constructor(@ApplicationContext private val context: Context) {
    private var roomData = WordsData().addData()
    private lateinit var fixedData: List<String>

    //ROOM ACTIONS
    //Local database creation
    val roomdDatabase = Room.databaseBuilder(
        context.applicationContext,
        RoomDatabaseWords::class.java,
        "RoomWords").build()
    val wordsDao = roomdDatabase.wordsDao()
    //In first run adding data to app
    fun addDataToApp(){
        CoroutineScope(Dispatchers.Main).launch {
            for (i in 0..roomData.size-1){
                fixedData = roomData[i].split(",").map { it.trim() }
                for(j in 0..fixedData.size-1){
                    wordsDao.addWord(
                        RoomWords(0,fixedData[j],"false",when(i){
                        0 ->"A1"
                        1 ->"A2"
                        2 ->"B1"
                        3 ->"B2"
                        4 ->"C1"
                        else->"Unknown"
                    })
                    )
                }

            }
        }
    }
    //CRUD for room
    fun roomSearchedWords(searchedWord: String): LiveData<List<RoomWords>> {
        return wordsDao.searchWord(searchedWord)
    }
    fun roomGetLevelWords(level: String): LiveData<List<RoomWords>> {
        return wordsDao.getLevelWords(level)
    }
    fun roomGetAllWords(): LiveData<List<RoomWords>> {
        return wordsDao.getAllWords()
    }
    fun roomAddWord(word: RoomWords){
        val job = CoroutineScope(Dispatchers.Main).launch {  wordsDao.addWord(word) }
    }
    fun roomDeleteAllWords(){
        val job =  CoroutineScope(Dispatchers.Main).launch {  wordsDao.deleteAllWords() }
    }
    fun roomDeleteWord(wordId:Int){
        val job = CoroutineScope(Dispatchers.Main).launch {  wordsDao.deleteWord(wordId) }
    }
    fun roomDeleteWordWithName(wordName:String){
        val job = CoroutineScope(Dispatchers.Main).launch {  wordsDao.deleteWordWithName(wordName) }
    }
    fun roomUpdateWord(word: RoomWords){
        val job = CoroutineScope(Dispatchers.Main).launch {  wordsDao.updateWord(word) }
    }


}