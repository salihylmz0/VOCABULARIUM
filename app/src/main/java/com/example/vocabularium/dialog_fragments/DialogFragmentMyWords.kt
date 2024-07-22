package com.example.vocabularium.dialog_fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.vocabularium.R
import com.example.vocabularium.adapters.WordMeaningsRVAdapter
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.databinding.FragmentDialogMyWordsBinding
import com.example.vocabularium.exceptions.MyWordsSingleton
import com.google.firebase.Firebase
import com.google.firebase.database.database


class DialogFragmentMyWords :DialogFragment() {
    private lateinit var design:FragmentDialogMyWordsBinding
    val animation = Attention()
    val viewModel: DialogFragmentMyWordsViewModel by viewModels ()
    var myWordSingleton=MyWordsSingleton
    var fbReference = Firebase.database.reference
    var soundUri:String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_dialog_my_words,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.myWordsDialogObject= this

        viewModel.getWordInfo(myWordSingleton.myWord.wordName)
        viewModel.apiData.observe(viewLifecycleOwner,{data->
            assignSound()
            RVassignments()
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt() // Ekran genişliğinin %85'i
            val height = (resources.displayMetrics.heightPixels * 0.85).toInt() // Ekran yüksekliğinin %75'i
            window.setLayout(width, height)
        }
    }

    fun learnt(){
        animate(design.learnt)
        viewModel.updateLearningStateOnFB(myWordSingleton.myWord,"true")
        dismiss()
    }
    fun notLearnt(){
        animate(design.notLearnt)
        viewModel.updateLearningStateOnFB(myWordSingleton.myWord,"false")
        dismiss()
    }
    fun animate(view:View){
        animation.Ruberband(view).apply { duration= 1000;start() }
    }
    fun updateTextViews(){
        design.wordName.text = myWordSingleton.myWord.wordName

    }
    fun RVassignments(){
        val dialogMyWordsMeaningAdapter = WordMeaningsRVAdapter(requireContext(),viewModel.apiData.value!!.meanings)
        design.meaningsRV.adapter = dialogMyWordsMeaningAdapter
        design.meaningsRV.setHasFixedSize(true)
        updateTextViews()
    }
    fun removeMyWord(){
        animation.Ruberband(design.removeMyWords).apply { duration=1000;start() }
        viewModel.deleteAWordFromFb(myWordSingleton.myWord)
        Toast.makeText(requireContext(),"This text was removed from My words...",Toast.LENGTH_SHORT).show()
        dismiss()
    }
    fun assignSound(){
        var sound:String = ""
        viewModel.apiData.value?.phonetics?.forEach {
            if(it.audio != ""){
                sound = it.audio
            }
        }
        soundUri = sound
    }
    fun pronounciationSound(){
        animation.Bounce(design.sound).apply {
            duration = 1000
            start()
        }
        if (soundUri != ""){
            val mediaPlayer = MediaPlayer().apply {
                setDataSource(soundUri)
                prepare()
                start()
            }
        }

    }

}