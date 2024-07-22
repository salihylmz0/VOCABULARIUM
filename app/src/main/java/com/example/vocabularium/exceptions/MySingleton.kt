package com.example.vocabularium.exceptions

import android.net.Uri
import com.example.vocabularium.models.AppWords

object MySingleton {
     var dataList : List<AppWords>? = null
     var stageValue:Int? = null
     var profileUri: Uri? = null
}