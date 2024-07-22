package com.example.vocabularium.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var tasarim:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        tasarim = DataBindingUtil.setContentView(this,R.layout.activity_splash)
        GlobalScope.launch {
            delay(2200)
            val intent=Intent(this@SplashActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
}