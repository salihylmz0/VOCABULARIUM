package com.example.vocabularium.repositories
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.vocabularium.R
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import com.example.vocabularium.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FirebaseDataRepository @Inject constructor(@ApplicationContext val context: Context) {
    var user : FirebaseUser?
    val auth = Firebase.auth
    val fbReference = Firebase.database.reference
    init {
        user = auth.currentUser
    }
    //Firebase Actions
    //CRUD firebase
    fun addWordToFB(word: FirebaseWords,control:MutableLiveData<Int>){
        if (user!= null){
            fbReference.child("Users")
                .child(user!!.uid)
                .child(word.wordLevel)
                .child(word.wordId)
                .setValue(word)
        }
    }

    fun getLevelWordsFromFb(level: String,
                            data: MutableLiveData<ArrayList<FirebaseWords>>){
        val words = ArrayList<FirebaseWords>()
        if (user != null){
            fbReference.child("Users")
                .child(user!!.uid)
                .child(level).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                        for (snapshot in datasnapshot.children){
                            val item = snapshot.getValue(FirebaseWords::class.java)
                            if (item != null) words.add(item)
                        }
                        data.value = words
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getAllWordsFromFb(data: MutableLiveData<ArrayList<FirebaseWords>>){
        val words = ArrayList<FirebaseWords>()
        if (user != null){
            fbReference.child("Users")
                .child(user!!.uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                        for (levelSnapshot in datasnapshot.children){
                            for (wordSnapshot in levelSnapshot.children){
                                val item = wordSnapshot.getValue(FirebaseWords::class.java)
                                if (item != null){
                                    words.add(item)
                                }
                            }
                        }
                        data.value = words
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
    fun deleteAWordFromFb(word:FirebaseWords){
        if(user !=null){
            fbReference.child("Users")
                .child(user!!.uid)
                .child(word.wordLevel)
                .child(word.wordId).removeValue()
        }
    }
    fun deleteAllWordsFromFB(){
        if(user != null){
            fbReference.child("Users")
                .child(user!!.uid).removeValue()

        }
    }

    fun addMyWordToFB(data:AppWords,message:MutableLiveData<String>){
        if(user!=null){
            fbReference.child("MyWords").child(user!!.uid).child(data.wordId.toString()).setValue(FirebaseWords(data.wordId.toString(),data.wordLevel,data.wordName,data.wordState.toString())).
            addOnCompleteListener {
                message.value = "This word was added to My Words..."
            }
        }
    }
    fun getMyWordsFromFB(data: MutableLiveData<ArrayList<FirebaseWords>>){
        val words = ArrayList<FirebaseWords>()
        if (user != null){
            fbReference.child("MyWords")
                .child(user!!.uid).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(datasnapshot: DataSnapshot) {
                            for (snapshot in datasnapshot.children){
                                val item = snapshot.getValue(FirebaseWords::class.java)
                                if (item != null) words.add(item)
                            }
                            data.value = words
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getUserInformation(userdata:MutableLiveData<User>){
        var userInfo = User()
        if (user != null){
            fbReference.child("User Information").child(user!!.uid).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot != null){
                        userInfo = snapshot.getValue(User::class.java)!!
                        if (userInfo != null) userdata.value = userInfo
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   Log.e("user Information error","${error.message}")
                }

            })
        }
    }


}