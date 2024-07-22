package com.example.vocabularium.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.vocabularium.R
import com.example.vocabularium.adapters.WordMeaningsRVAdapter
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.databinding.FragmentDictionaryBinding
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.example.vocabularium.firebase.FirebaseWords
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class DictionaryFragment : Fragment() {

    private val animation = Attention()
    val viewModel: DictionaryFragmentViewModel by viewModels()
    private lateinit var design: FragmentDictionaryBinding
    val user = Firebase.auth.currentUser
    var initializedObserver = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_dictionary,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.dictionaryFragmentObject = this

        viewModel.apiData.observe(viewLifecycleOwner,{data->
            if (initializedObserver) {
                val practiceRVAdapter = WordMeaningsRVAdapter(requireContext(),data.meanings)
                design.dictionaryRV.adapter = practiceRVAdapter
                design.dictionaryRV.setHasFixedSize(true)
            }else initializedObserver = true

        })
        viewModel.toastMessage.observe(viewLifecycleOwner,{
            if (initializedObserver)  it?.let { message-> if (message != "") showToast(message) }
            else initializedObserver = true
        })
    }
    fun getWordMeaning(){
        user?.let {
            viewModel.clearMessage()
            viewModel.getWordInfo(design.searchText.text.toString())
        }
    }
    fun addMyWord(){
        viewModel.clearMessage()
        if (user!= null){
            animation.Ruberband(design.addMyWordsButton).apply { duration=1000 ;start()}
            if ((design.searchText.text.toString() != "") && viewModel.apiData.value != null){
                viewModel.addMyWordToFB(FirebaseWords(design.searchText.text.toString(),"dictionary",design.searchText.text.toString(),"false"))
            }else{ showToast("No data to add ...")}

        }else{
            val dialog = LoginDialogFragment()
            dialog.show(parentFragmentManager,"login dialog")
        }

    }
    fun showToast(message: String) {
        Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
        viewModel.toastMessage.value = null
    }
}