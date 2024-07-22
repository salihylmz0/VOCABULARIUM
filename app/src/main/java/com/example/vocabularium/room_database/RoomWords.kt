package com.example.vocabularium.room_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity("RoomWords")
data class RoomWords(@PrimaryKey(autoGenerate = true)
                     @ColumnInfo("wordId")  @NotNull var wordId: Int,
                     @ColumnInfo("wordName")@NotNull var wordName: String,
                     @ColumnInfo("wordState")@NotNull var wordState: String,
                     @ColumnInfo("wordLevel")@NotNull var wordLevel: String){
}