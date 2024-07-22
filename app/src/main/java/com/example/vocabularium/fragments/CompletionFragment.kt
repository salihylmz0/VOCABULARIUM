package com.example.vocabularium.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.activities.MainActivity
import com.example.vocabularium.animations.Rotate
import com.example.vocabularium.animations.Zoom
import com.example.vocabularium.databinding.FragmentCompletionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CompletionFragment : Fragment() {
    private lateinit var design: FragmentCompletionBinding
    private val animation2 = Rotate()
    private val animation1 = Zoom()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        design = DataBindingUtil.inflate(inflater,R.layout.fragment_completion,container,false)
        return design.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        design.completionFragmentObject = this
        animation1.InDown(design.thick).apply {
            GlobalScope.launch {
                delay(1500)
            }
            duration = 2000
            start()
        }

    }
    fun getToStages(){
        val intent = Intent(requireContext(),MainActivity::class.java)
        startActivity(intent)
    }
}