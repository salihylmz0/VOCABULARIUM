package com.example.vocabularium.dialog_fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.vocabularium.R
import com.example.vocabularium.activities.AuthenticationActivity
import com.example.vocabularium.databinding.FragmentLoginDialogBinding


class LoginDialogFragment : DialogFragment() {
    private lateinit var tasarim:FragmentLoginDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_login_dialog,container,false)
        return tasarim.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasarim.loginDialogFragmentObject = this
        dialog?.setCancelable(true)
        tasarim.closeDialog.setOnClickListener {
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