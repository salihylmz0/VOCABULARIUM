package com.example.vocabularium.dialog_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.vocabularium.R
import com.example.vocabularium.animations.Flip

import com.example.vocabularium.databinding.FragmentBackPressDialogBinding


class BackPressDialogFragment : DialogFragment() {

    val animation = Flip()
    private lateinit var tasarim: FragmentBackPressDialogBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_back_press_dialog,container,false)
        return tasarim.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasarim.backPressDialogObject = this
        animation.InX(tasarim.root).apply { duration=1000;start() }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.let {window->
            val width = (resources.displayMetrics.widthPixels*0.7).toInt()
            val height = (resources.displayMetrics.heightPixels*0.3).toInt()
            window.setLayout(width,height)
        }
    }
    fun yesButton(){
        requireActivity().finishAffinity()
    }
    fun noButton(){
        dismiss()
    }
}