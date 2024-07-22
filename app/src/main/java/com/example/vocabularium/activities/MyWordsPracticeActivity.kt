package com.example.vocabularium.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivityPracticeBinding

class MyWordsPracticeActivity : AppCompatActivity() {
    private lateinit var tasarim:ActivityPracticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this,R.color.backgroundtype2)
        enableEdgeToEdge()
        tasarim = DataBindingUtil.setContentView(this,R.layout.activity_practice)


    }
}