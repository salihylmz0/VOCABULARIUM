package com.example.vocabularium.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.vocabularium.R
import com.example.vocabularium.adapters.LevelsRecyclerViewAdapter
import com.example.vocabularium.adapters.StageRVAdapter
import com.example.vocabularium.databinding.FragmentHomeBinding
import com.example.vocabularium.dialog_fragments.BackPressDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var design: FragmentHomeBinding
    private lateinit var levelsRVA:LevelsRecyclerViewAdapter
    private lateinit var stageRVA:StageRVAdapter
    var user = MutableLiveData<FirebaseUser?>()
    val auth = FirebaseAuth.getInstance()
    val fbReference = Firebase.database.reference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel.deleteAllWordsFromFB()
        user.value = auth.currentUser
        updateUserEmail()

        //Checking wheter logged in and act accordingly
        if(user.value != null){
            progressBarOn()
            viewModel.getLevelWordsFromFB()
        }else {
            progressBarOn()
            viewModel.getLevelWordsFromRoom()
        }

        //Shared Preferences for controlling isFirst Opening the app
        val sp = activity?.getSharedPreferences("isFirstRun",Context.MODE_PRIVATE)
        //Codes to perform when app first run after install
        if (sp != null) {
            if (sp.getBoolean("isFirstRun",true)){
                val job = CoroutineScope(Dispatchers.Main).launch { viewModel.addDataToAppRoom() }
            }
            with(sp.edit()){
                putBoolean("isFirstRun",false)
                apply()
            }
        }
        backPressOut()

        //Levels Recycler View
        rvLevel()
        //Stages Recycler View
        viewModel.levelWordsAppData.observe(viewLifecycleOwner,{
            progressBarOff()
            rvStage()
        })
        viewModel.levelValue.observe(viewLifecycleOwner,{
            progressBarOn()
            if (user.value != null) viewModel.getLevelWordsFromFB()
            else viewModel.getLevelWordsFromRoom()
        })
        viewModel.toastMessage.observe(viewLifecycleOwner,{message->
            showToast(message)
        })
        levelsRVA.onItemClick ={level->
            fun setLevel(level:String){
                viewModel.levelValue.value = level
            }
            when(level.levelName){
                "Beginner"->setLevel("A1")
                "Elementary"->setLevel("A2")
                "Intermediate"->setLevel("B1")
                "Upper Intermediate"->setLevel("B2")
                "Advanced"->setLevel("C1")
            }
        }
    }
    fun rvStage(){
        stageRVA = StageRVAdapter(requireContext(),viewModel.levelWordsAppData.value!!)
        design.recyclerViewStages.adapter = stageRVA
        design.recyclerViewStages.setHasFixedSize(true)
    }
    fun rvLevel(){
        levelsRVA = LevelsRecyclerViewAdapter(requireContext(), viewModel.levelsDataHFVM)
        design.recyclerViewLevels.adapter = levelsRVA
        design.recyclerViewLevels.setHasFixedSize(true)
    }
    fun progressBarOn(){
        design.progressBarHomeFragment.visibility = View.VISIBLE
    }
    fun progressBarOff(){
        design.progressBarHomeFragment.visibility = View.INVISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()

        //Remove observeForevers
        viewModel.removeObservation(viewModel.searchedDataobserver)
        viewModel.removeObservation2(viewModel.levelFBObserver)
    }
    fun backPressOut(){
        val callBack = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            val dialog = BackPressDialogFragment()
            dialog.show(parentFragmentManager,"backPressOut")
        }
    }
    fun updateUserEmail(){
        user.value?.let { user-> viewModel.updateUserEmail(user.email.toString())}
    }
    fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }
}