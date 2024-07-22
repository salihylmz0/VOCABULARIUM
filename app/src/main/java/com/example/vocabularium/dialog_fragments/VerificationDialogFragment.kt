package com.example.vocabularium.dialog_fragments

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.vocabularium.R
import com.example.vocabularium.databinding.FragmentVerificationDialogFragmentBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class VerificationDialogFragment : DialogFragment() {
    private lateinit var design: FragmentVerificationDialogFragmentBinding
    val user = Firebase.auth.currentUser
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_verification_dialog_fragment,container,false)
       return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.verificationDialogObject = this
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window->
            val width = (resources.displayMetrics.widthPixels*0.8).toInt()
            val height = (resources.displayMetrics.heightPixels*.3).toInt()
            window.setLayout(width,height)
        }
    }


    fun sendEmail(){
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"Mail was sent...",Toast.LENGTH_SHORT).show()
                  dialog?.dismiss()
                }
            }
    }

}