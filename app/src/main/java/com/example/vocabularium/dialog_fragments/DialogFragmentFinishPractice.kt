package com.example.vocabularium.dialog_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.vocabularium.R
import com.example.vocabularium.activities.MainActivity
import com.example.vocabularium.databinding.FragmentDialogBinding

class DialogFragmentFinishPractice : DialogFragment() {
        private lateinit var design: FragmentDialogBinding
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            design = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog,container,false)
            return design.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            design.dialogFragmentObject = this

        }



        fun yesButton(){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        fun continueButton(){
            dismiss()
        }
}