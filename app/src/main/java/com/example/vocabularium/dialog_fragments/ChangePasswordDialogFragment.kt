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
import com.example.vocabularium.R
import com.example.vocabularium.databinding.FragmentChangePasswordDialogBinding
import com.example.vocabularium.models.User
import dagger.hilt.android.AndroidEntryPoint
import org.checkerframework.framework.qual.AnnotatedFor

@AndroidEntryPoint
class ChangePasswordDialogFragment : DialogFragment() {

    private lateinit var design:FragmentChangePasswordDialogBinding
    private val viewModel:ChangePasswordViewModel by viewModels()
    private var hasInitialized = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_change_password_dialog,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.changePassworddObject = this
        viewModel.getUserInfo()
        viewModel.userValue.observe(viewLifecycleOwner,{
            currentPassword(it)
        })
        viewModel.toastMessage.observe(viewLifecycleOwner,{message->
            if (hasInitialized){
                showToast(message)
            }else hasInitialized = true

        })
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.let {window->
            val width = (resources.displayMetrics.widthPixels*0.9).toInt()
            val height = (resources.displayMetrics.heightPixels*0.5).toInt()
            window.setLayout(width,height)
        }
    }
    fun currentPassword(user: User){
        design.validPassword.setText(user.userPassword)
    }
    fun changePassword(){
        viewModel.applyPasswordChanges(design.validPassword,design.newPassword,design.confirmPassword)
    }
    fun showToast(message:String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}