package com.example.vocabularium.fragments

import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.vocabularium.R
import com.example.vocabularium.adapters.WordMeaningsRVAdapter
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.databinding.FragmentDictionaryBinding
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.example.vocabularium.firebase.FirebaseWords
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DictionaryFragment : Fragment() {

    private val animation = Attention()
    val viewModel: DictionaryFragmentViewModel by viewModels()
    private lateinit var tasarim: FragmentDictionaryBinding
    val user = Firebase.auth.currentUser
    var initializedObserver = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_dictionary,container,false)
        return tasarim.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasarim.dictionaryFragmentObject = this

        viewModel.apiData.observe(viewLifecycleOwner,{data->
            if (initializedObserver) {
                val practiceRVAdapter = WordMeaningsRVAdapter(requireContext(),data.meanings)
                tasarim.dictionaryRV.adapter = practiceRVAdapter
                tasarim.dictionaryRV.setHasFixedSize(true)
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
            viewModel.getWordInfo(tasarim.searchText.text.toString())
        }
    }
    fun addMyWord(){
        viewModel.clearMessage()
        if (user!= null){
            animation.Ruberband(tasarim.addMyWordsButton).apply { duration=1000 ;start()}
            if ((tasarim.searchText.text.toString() != "") && viewModel.apiData.value != null){
                viewModel.addMyWordToFB(FirebaseWords(tasarim.searchText.text.toString(),"dictionary",tasarim.searchText.text.toString(),"false"))
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