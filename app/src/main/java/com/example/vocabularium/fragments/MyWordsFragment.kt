package com.example.vocabularium.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.vocabularium.R
import com.example.vocabularium.adapters.MyWordsRVAdapter
import com.example.vocabularium.databinding.FragmentMyWordsBinding
import com.example.vocabularium.dialog_fragments.DialogFragmentMyWords
import com.example.vocabularium.dialog_fragments.LoginDialogFragment
import com.example.vocabularium.exceptions.MyWordsSingleton
import com.example.vocabularium.firebase.FirebaseWords
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWordsFragment : Fragment() {
    private val user = Firebase.auth.currentUser

    private val fbReference = Firebase.database.reference
    private lateinit var design: FragmentMyWordsBinding
    private  val viewModel: MyWordsFragmentViewModel by viewModels()
    var myWordsPractice = MyWordsSingleton
    var initializedObserver = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_my_words,container,false)
        return design.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.myWordsFragmentObject = this
        if(user == null){
            isUserLoggedIn()
        }else{
            viewModel.getMyWordsFromFB()
            viewModel.myWordsFB.observe(viewLifecycleOwner,{myWordsData->
                myWordsPractice.practiceWords = myWordsData
                rvMyWords(viewModel.myWordsFB.value!!)
            })

            fbReference.child("MyWords").child(user?.uid.toString()).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewModel.getMyWordsFromFB()
                }
                override fun onCancelled(error: DatabaseError) { }
            })
            viewModel.searchedWords.observe(viewLifecycleOwner,{searchedWords->
                if (initializedObserver){
                    rvMyWords(searchedWords)
                }else initializedObserver = true
            })
            editText()
        }
    }
    fun rvMyWords(data:ArrayList<FirebaseWords>){
        updateWordsNumber(data)
        val myWordsAdapter = MyWordsRVAdapter(requireContext(),data)
        design.myWordsRV.adapter = myWordsAdapter
        design.myWordsRV.setHasFixedSize(true)
        myWordsAdapter.myWordValue ={
            val dialog = DialogFragmentMyWords()
            dialog.show(parentFragmentManager,"my word dialog")
        }

    }
    fun filterMenu(){
        user?.let {
            val filterMenu = PopupMenu(requireActivity(),design.filter)
            filterMenu.inflate(R.menu.filter_menu)
            filterMenu.setOnMenuItemClickListener {menuItem->
                when(menuItem.itemId){
                    R.id.all->{ rvMyWords(viewModel.myWordsFB.value!!);assignPracticeWords(viewModel.myWordsFB.value!!);true}
                    R.id.beginner->{rvMyWords(filterData("A1"));assignPracticeWords(filterData("A1"));true}
                    R.id.elementary->{rvMyWords(filterData("A2"));assignPracticeWords(filterData("A2"));true}
                    R.id.intermediate->{rvMyWords(filterData("B1"));assignPracticeWords(filterData("B1"));true}
                    R.id.upperIntermediate->{rvMyWords(filterData("B2"));assignPracticeWords(filterData(",B2"));true}
                    R.id.advanced->{rvMyWords(filterData("C1"));assignPracticeWords(filterData("C1"));true}
                    R.id.dictionary->{rvMyWords(filterData("dictionary"));assignPracticeWords(filterData("dictionary"));true}
                    else->false
                }
            }
            filterMenu.show()
        }

    }
    fun getToPractice(){
        user?.let {
            Navigation.findNavController(design.practiceButton).navigate(R.id.action_myWordsFragment_to_myWordsPracticeActivity)
        }
    }
    fun cleanMyWords(){
        user?.let {
            val settings = PopupMenu(requireActivity(),design.threeDot)
            settings.inflate(R.menu.menu_threedot_mywords)
            settings.setOnMenuItemClickListener { menuItem->
                when(menuItem.itemId){
                    R.id.clean->{
                        viewModel.cleanMyWords()
                        Toast.makeText(requireActivity(),"My words was cleaned",Toast.LENGTH_SHORT).show()
                        true}
                    else -> false
                }
            }
            settings.show()
        }

    }
    fun searchWord(){
        user?.let {
            viewModel.searchWords(design.searchfromMw.text.toString())
        }

    }
    fun isUserLoggedIn(){
        val dialog = LoginDialogFragment()
        dialog.show(parentFragmentManager,"login dialog")
    }
    fun updateWordsNumber(data:ArrayList<FirebaseWords>){
        design.mywordsNumber.text = data.size.toString()+" words"
    }
    fun filterData(select:String):ArrayList<FirebaseWords>{
        val temporary = viewModel.myWordsFB.value!!.filter { it.wordLevel == select } as ArrayList
        return temporary
    }
    fun assignPracticeWords(data:ArrayList<FirebaseWords>){
        var practice = MyWordsSingleton
        practice.practiceWords = data
    }
    fun editText(){
        design.searchfromMw.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty())rvMyWords(viewModel.myWordsFB.value!!)
            }

        })
    }
}