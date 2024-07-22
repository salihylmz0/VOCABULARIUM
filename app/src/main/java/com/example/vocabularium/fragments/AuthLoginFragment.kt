package com.example.vocabularium.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.activities.MainActivity
import com.example.vocabularium.databinding.FragmentAuthLoginBinding
import com.example.vocabularium.dialog_fragments.ForgetPasswordDialogFragment
import com.example.vocabularium.dialog_fragments.VerificationDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthLoginFragment : Fragment() {

    val user = Firebase.auth.currentUser
    private lateinit var design: FragmentAuthLoginBinding
    private lateinit var viewModel: AuthLoginFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_auth_login,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthLoginFragmentViewModel::class.java)
        design.authLoginFragmentObject = this
        viewModel.isLoginSuccessful.observe(viewLifecycleOwner,{
            if (it == true){
                viewModel.isLoginSuccessful.value = false
                goToHomeFragment()
            }
        })
        viewModel.isNotEmailVerified.observe(viewLifecycleOwner,{
            if (it == true){
                val dialog = VerificationDialogFragment()
                dialog.show(parentFragmentManager,"dialog_verification")
            }
        })
        viewModel.toastMessage.observe(viewLifecycleOwner,{
            showToast(it)
        })
        design.forgetPassword.setOnClickListener {
            val dialog = ForgetPasswordDialogFragment()
            dialog.show(parentFragmentManager,"dialog")
        }
        backPress()
    }

    fun backPress(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
           val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun goToHomeFragment(){
        val intent = Intent(activity,MainActivity::class.java)
        startActivity(intent)
    }
    fun goToRegisterFragment(){
        viewModel.getToRegisterFragment(design.registerText)
    }
    fun login(email:EditText,password:EditText){
            viewModel.login(email,password)
    }
    fun showToast(message: String){
        Toast.makeText(requireActivity(),"${message}",Toast.LENGTH_SHORT).show()
    }

}

