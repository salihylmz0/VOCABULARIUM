package com.example.vocabularium.room_database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordsDao {
    @Query("SELECT * FROM RoomWords")
    fun getAllWords(): LiveData<List<RoomWords>>
    @Insert
    suspend fun addWord(word:RoomWords)

    @Update
    suspend fun updateWord(word: RoomWords)

    @Query("DELETE FROM RoomWords")
    suspend fun deleteAllWords()
    @Query("DELETE FROM RoomWords WHERE wordId = :wordId")
    suspend fun deleteWord(wordId: Int)
    @Query("DELETE FROM RoomWords WHERE wordName = :wordName")
    suspend fun deleteWordWithName(wordName: String)

    @Query("SELECT * FROM  RoomWords WHERE wordLevel = :level ")
    fun getLevelWords(level: String): LiveData<List<RoomWords>>

    @Query("SELECT * FROM RoomWords WHERE wordName like '%' || :searchedWord || '%'")
    fun searchWord(searchedWord: String): LiveData<List<RoomWords>>
}