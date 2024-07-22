package com.example.vocabularium.dialog_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.vocabularium.R
import com.example.vocabularium.databinding.FragmentForgetPasswordDialogBinding


class ForgetPasswordDialogFragment : DialogFragment() {
    private val viewModel:ForgetPasswordDialogViewModel by viewModels()
    private lateinit var design:FragmentForgetPasswordDialogBinding
    private var hasInitialized = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        design = DataBindingUtil.inflate(inflater, R.layout.fragment_forget_password_dialog, container, false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.forgetPasswordDialogFragmentObject = this
        viewModel.toastMessage.observe(viewLifecycleOwner,{message->
            if (hasInitialized){
                showToast(message)
            }else hasInitialized = true
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {window->
            val width = (resources.displayMetrics.widthPixels*0.8).toInt()
            val height = (resources.displayMetrics.heightPixels*0.25).toInt()
            window.setLayout(width,height)
        }
    }
    fun sentMail(){
        viewModel.resetEmail(design.email.text.toString())
    }
    fun dismis(){
        dialog?.dismiss()
    }
    fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }
}