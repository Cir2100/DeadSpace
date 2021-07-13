package com.example.deadspace.ui.schedule

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class AddRaspActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_rasp_activity)
    }

    fun onClickBackAddRasp(view:View)
    {
        val backAddRaspIntent = Intent(this, ScheduleActivity::class.java)
        startActivity(backAddRaspIntent)
    }
}