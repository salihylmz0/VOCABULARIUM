package com.example.vocabularium.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.vocabularium.R
import com.example.vocabularium.activities.AuthenticationActivity
import com.example.vocabularium.databinding.FragmentAuthRegisterBinding
import com.example.vocabularium.databinding.FragmentSettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthRegisterFragment : Fragment() {

    private  val viewModel :AuthRegisterFragmentViewModel by viewModels ()
    private lateinit var tasarim: FragmentAuthRegisterBinding
    var auth: FirebaseAuth = Firebase.auth
    val fbReference = Firebase.database.reference
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_auth_register,container,false)
        return tasarim.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasarim.authRegisterFragmentObject= this


        viewModel.toastMessage.observe(viewLifecycleOwner){
             showToastLong(it)
        }



    }


    fun getToLoginFragment(){
        val intent = Intent(requireActivity(),AuthenticationActivity::class.java)
        startActivity(intent)
    }
    fun registerAMember(name: EditText, emailOrUsername: EditText, password: EditText, passwordConfirmation: EditText){
        viewModel.signUpNewUser(name, emailOrUsername, password, passwordConfirmation)
    }

    fun showToastLong(message: String){
        Toast.makeText(requireActivity(),"${message}",Toast.LENGTH_LONG).show()
    }
    fun showToastShort(message: String){
        Toast.makeText(requireActivity(),"${message}",Toast.LENGTH_SHORT).show()
    }
}