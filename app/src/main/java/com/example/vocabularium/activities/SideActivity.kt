package com.example.vocabularium.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivitySideBinding
import com.example.vocabularium.exceptions.MySingleton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SideActivity : AppCompatActivity() {
    private lateinit var viewModel: SideActivityViewModel
    private lateinit var tasarim : ActivitySideBinding
    private  var myObject=MySingleton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this,R.color.backgroundtype2)//I tried to change this on Xml but it did not work
        tasarim = DataBindingUtil.setContentView(this,R.layout.activity_side)

    }
}