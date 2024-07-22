package com.example.vocabularium.fragments

import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularium.R
import com.example.vocabularium.adapters.WordMeaningsRVAdapter
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.animations.Zoom
import com.example.vocabularium.databinding.FragmentStudyBinding
import com.example.vocabularium.dialog_fragments.DialogFragmentFinishPractice
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.example.vocabularium.exceptions.MySingleton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudyFragment : Fragment() {
    private val viewModel: StudyFragmentViewModel by viewModels()
    private lateinit var design:FragmentStudyBinding
    private val studyingData = MySingleton.dataList
    private val animationtoTransition = Zoom()
    val animation = Attention()
    var user = Firebase.auth.currentUser
    var soundUri:String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_study,container,false)
        return design.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.studyFragmentObject = this
        updateToolbar()
        back_forwardVisibilty()

        viewModel.position.observe(viewLifecycleOwner,{position->
            if(position<viewModel.studyingData.value!!.size){
                viewModel.getWordInfo(studyingData!![position].wordName)
                viewModel.updateData(design.wordName)
                updateThumbColor()
                updatePositionText()
                back_forwardVisibilty()
            }
        })

        viewModel.apiData.observe(viewLifecycleOwner,{apiData->
            val meaningAdapter = WordMeaningsRVAdapter(requireContext(),apiData.meanings)
            design.meaningsRV.layoutManager = LinearLayoutManager(requireContext())
            design.meaningsRV.adapter = meaningAdapter
            design.meaningsRV.setHasFixedSize(true)
            assignSound()
        })
        viewModel.toastMessage.observe(viewLifecycleOwner,{
            showToast(it)
        })
    }
    fun updateToolbar(){
        var wordLevel = studyingData?.get(0)?.wordLevel
        design.customToolbar.titleLevel.text = when(wordLevel){
            "A1"->"Beginner"
            "A2"->"Elementary"
            "B1"->"Intermediate"
            "B2"->"UpperIntermediate"
            "C1"->"Advanced"
            else->null
        }
        design.customToolbar.titleStage.text = "Stage " +MySingleton.stageValue.toString()
    }
    fun updateThumbColor(){
        if (viewModel.dataKeeper.get(viewModel.position.value!!).wordState == true) design.learnt.setImageResource(R.drawable.thumb_up_svgrepo_com)
        else design.learnt.setImageResource(R.drawable.thumb_up_svgrepo_com_notlearnt)
    }
    fun updatePositionText(){
        design.wordPositon.text = (viewModel.position.value?.toInt()!! + 1).toString()+"/"+viewModel.dataKeeper.size.toString()
    }
    fun goForward(){
        viewModel.goForward(design.studyCard)
        CompleteStage()
        animation.Ruberband(design.forwaderButton).apply { duration = 400;start() }
    }
    fun goBack(){
        viewModel.goBack(design.studyCard)
        animation.Ruberband(design.backButton).apply { duration = 400;start() }
    }
    fun leaveTheProcess(){
        val fragment = DialogFragmentFinishPractice()
        fragment.show(parentFragmentManager,"customDialog")
    }
    fun notLearntButton(){
        if (user!= null){
        viewModel.updateKeeperValue(viewModel.position.value!!,false)
        viewModel.goForward(design.studyCard)
        CompleteStage()
        animation.Wobble(design.notLearnt).apply { duration = 1000; start()}
        }else loginAsk()
    }
    fun learntButton(){
        if (user!= null){
            viewModel.updateKeeperValue(viewModel.position.value!!,true)
            viewModel.goForward(design.studyCard)
            CompleteStage()
            animation.Ruberband(design.learnt).apply { duration = 700; start() }
        }else loginAsk()

    }
    fun back_forwardVisibilty(){
        if (viewModel.position.value == 0)design.backButton.visibility= View.INVISIBLE
        else  design.backButton.visibility = View.VISIBLE
        if (viewModel.position.value == 9)design.forwaderButton.visibility= View.INVISIBLE
        else design.forwaderButton.visibility = View.VISIBLE
    }
    fun CompleteStage(){
        if (viewModel.position.value == 10){
            Navigation.findNavController(design.learnt).navigate(R.id.action_studyFragment_to_completionFragment)
            if (user != null){
                for (i in viewModel.dataKeeper){
                    viewModel.updateLearningStateOnFB(i)
                }
            }else{
                viewModel.updateLearningStateOnRoom(viewModel.dataKeeper)
            }
        }
    }
    fun addMyWord(){
        if (user!= null){
        animation.Ruberband(design.addMyWordsButton).apply { duration=1000 ;start()}
        viewModel.addMyWordToFB(viewModel.dataKeeper.get(viewModel.position.value!!))
        }else loginAsk()
    }
    fun loginAsk(){
        val dialog= LoginDialogFragment()
        dialog.show(parentFragmentManager,"login dialog")
    }
    fun showToast(message:String){
        Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
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