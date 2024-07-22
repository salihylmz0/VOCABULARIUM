package com.example.vocabularium.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [RoomWords::class], version = 1)
abstract class RoomDatabaseWords: RoomDatabase() {
    abstract fun wordsDao(): WordsDao

}