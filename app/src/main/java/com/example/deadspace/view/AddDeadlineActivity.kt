package com.example.deadspace.view

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class AddDeadlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_deadline)
    }

    fun ClickBackAddDeadlines(view:View)
    {
        val BackAddDeadlinesIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(BackAddDeadlinesIntent)
    }
}