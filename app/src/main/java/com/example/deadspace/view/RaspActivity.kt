package com.example.deadspace.view

import android.content.Intent
import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class RaspActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rasp)
    }
    fun ClickBackListRasp(view: View)
    {
        val BackListRaspIntent = Intent(this, StartActivity::class.java)
        startActivity(BackListRaspIntent)
    }
}