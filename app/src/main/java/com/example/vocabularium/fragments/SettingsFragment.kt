package com.example.vocabularium.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsFragmentViewModel
    private lateinit var tasarim: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_settings,container,false)
        return tasarim.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsFragmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}