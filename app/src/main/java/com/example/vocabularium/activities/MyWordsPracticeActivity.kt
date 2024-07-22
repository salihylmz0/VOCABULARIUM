package com.example.vocabularium.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.vocabularium.R
import com.example.vocabularium.databinding.ActivityPracticeBinding

class MyWordsPracticeActivity : AppCompatActivity() {
    private lateinit var design:ActivityPracticeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this,R.color.backgroundtype2)
        enableEdgeToEdge()
        design = DataBindingUtil.setContentView(this,R.layout.activity_practice)


    }
}