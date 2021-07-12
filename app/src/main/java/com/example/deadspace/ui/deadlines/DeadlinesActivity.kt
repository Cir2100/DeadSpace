package com.example.deadspace.ui.deadlines

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.example.deadspace.ui.main.StartActivity


class DeadlinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deadlines)
    }

    fun ClickAddDeadline(view: View)
    {
        val AddDeadlineAddDeadlineIntent = Intent(this, AddDeadlineActivity::class.java)
        startActivity(AddDeadlineAddDeadlineIntent)
    }

    fun ClickBackListDeadlines(view:View)
    {
        val BackListDeadlinesIntent = Intent(this, StartActivity::class.java)
        startActivity(BackListDeadlinesIntent)
    }
}