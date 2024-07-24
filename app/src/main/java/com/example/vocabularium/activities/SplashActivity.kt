package com.example.vocabularium.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var design:ActivitySplashBinding
    private val viewModel:SplashActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        design = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        viewModel.allRoomWords.observe(this@SplashActivity,{
            viewModel.checkFirstLogin()
        })
        GlobalScope.launch {
            delay(2500)
            val intent=Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
}