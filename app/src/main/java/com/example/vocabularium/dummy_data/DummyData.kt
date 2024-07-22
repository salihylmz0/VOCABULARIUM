package com.example.vocabularium.dummy_data

import android.content.Context
import com.example.vocabularium.R
import com.example.vocabularium.models.LanguageLevels
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DummyData@Inject constructor(@ApplicationContext private val context: Context) {
    private val levelsDataDD = ArrayList<LanguageLevels>()
    private val levelsArray= context.resources.getStringArray(R.array.language_levels)
    private val getPicturesID = ArrayList<Int>()
    private val picturesId : ArrayList<Int>


    init {
        picturesId = getPictureIDs()
    }

    fun getPictureIDs(): ArrayList<Int>{
        getPicturesID.add(R.drawable.level_beginner)
        getPicturesID.add(R.drawable.level_elementary)
        getPicturesID.add(R.drawable.level_intermediate)
        getPicturesID.add(R.drawable.level_upper_intermediate)
        getPicturesID.add(R.drawable.levels_advanced)

        return getPicturesID
    }

    fun getLevelValuesDD():ArrayList<LanguageLevels>{

        for (i in 0 until levelsArray.size){
            levelsDataDD.add(LanguageLevels(levelsArray[i],picturesId[i]))
        }
        return levelsDataDD
    }
}