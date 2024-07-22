package com.example.vocabularium.dialog_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.vocabularium.R
import com.example.vocabularium.activities.AuthenticationActivity
import com.example.vocabularium.databinding.FragmentLoginDialogBinding


class LoginDialogFragment : DialogFragment() {
    private lateinit var design:FragmentLoginDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_login_dialog,container,false)
        return design.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.loginDialogFragmentObject = this
        dialog?.setCancelable(true)
        design.closeDialog.setOnClickListener {
            dismiss()

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            val width = (resources.displayMetrics.widthPixels * 0.85).toInt() // Ekran genişliğinin %
            val height = (resources.displayMetrics.heightPixels * 0.45).toInt() // Ekran yüksekliğinin %
            window.setLayout(width, height)
        }
    }
    fun getToLogin(){
        val intent = Intent(requireActivity(),AuthenticationActivity::class.java)
        startActivity(intent)
    }
    fun getToRegister(){

    }
}