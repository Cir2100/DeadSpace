package com.example.deadspace.ui.schedule

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class AddRaspActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rasp)
    }

    fun ClickBackAddRasp(view:View)
    {
        val BackAddRaspIntent = Intent(this, RaspActivity::class.java)
        startActivity(BackAddRaspIntent)
    }
}