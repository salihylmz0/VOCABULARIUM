package com.example.vocabularium.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.vocabularium.R
import com.example.vocabularium.activities.MainActivity
import com.example.vocabularium.adapters.WordMeaningsRVAdapter
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.databinding.FragmentMyWordsPracticeBinding
import com.example.vocabularium.retrofit.DictionaryResponse

class MyWordsPracticeFragment : Fragment() {

    private val viewModel:MyWordsPracticeViewModel by viewModels()
    private lateinit var design:FragmentMyWordsPracticeBinding
    val animation = Attention()
    var soundUri:String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_my_words_practice,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.myWordsPracticeObject = this

        viewModel.position.observe(viewLifecycleOwner,{position->
            viewModel.getWordInfo(viewModel.practiceWords.get(position).wordName)
            back_forwardVisibilty()
            updatePositionText()
            updateThumbColor()
        })

        viewModel.apiData.observe(viewLifecycleOwner,{data->
            assignSound()
            rvPracticeAssign(data)
        })
    }

    fun forwardButton(){
        viewModel.forward(design.studyCard)
        animation.Ruberband(design.forwaderButton).apply { duration = 400;start() }
        updateThumbColor()
    }
    fun backButton(){
        viewModel.back(design.studyCard)
        animation.Ruberband(design.backButton).apply { duration = 400;start() }
        updateThumbColor()
    }
    fun learnt(){
        viewModel.position.value?.let {
            if (it < viewModel.keeper.size){
                viewModel.forward(design.studyCard)
                viewModel.updateLearningStateOnFB(viewModel.practiceWords.get(viewModel.position.value!!),"true")
                viewModel.keeper[viewModel.position.value!!] = "true" }
            }
        animation.Ruberband(design.learnt).apply { duration = 700;start()
        }


    }
    fun notLearnt(){
        viewModel.position.value?.let {
            if (it < viewModel.keeper.size){
                viewModel.forward(design.studyCard)
                viewModel.updateLearningStateOnFB(viewModel.practiceWords.get(viewModel.position.value!!),"false")
                viewModel.keeper[viewModel.position.value!!] = "false"
            }

            animation.Ruberband(design.notLearnt).apply { duration = 700;start() }
        }

    }
    fun rvPracticeAssign(data:DictionaryResponse){
        val practiceRV = WordMeaningsRVAdapter(requireContext(),data.meanings)
        design.meaningsRV.adapter = practiceRV
        design.meaningsRV.setHasFixedSize(true)
        design.wordName.text = data.word
    }
    fun back_forwardVisibilty(){
        if (viewModel.position.value == 0)design.backButton.visibility= View.INVISIBLE
        else  design.backButton.visibility = View.VISIBLE
        if (viewModel.position.value == viewModel.keeper.size-1){
            design.forwaderButton.visibility= View.INVISIBLE
            design.getButton?.visibility = View.VISIBLE
        } else{
            design.forwaderButton.visibility = View.VISIBLE
            design.getButton?.visibility = View.INVISIBLE
        }
    }
    fun updateThumbColor(){
        if (viewModel.keeper.get(viewModel.position.value!!)== "true") design.learnt.setImageResource(R.drawable.thumb_up_svgrepo_com)
        else design.learnt.setImageResource(R.drawable.thumb_up_svgrepo_com_notlearnt)
    }
    fun updatePositionText(){
        design.wordPositon.text = (viewModel.position.value?.toInt()!! + 1).toString()+"/"+viewModel.keeper.size.toString()
    }
    fun getToMyWords(){
        val intent = Intent(requireContext(),MainActivity::class.java)
        startActivity(intent)
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