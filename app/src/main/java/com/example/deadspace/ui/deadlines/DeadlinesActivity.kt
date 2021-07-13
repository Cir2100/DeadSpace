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
        setContentView(R.layout.deadlines_activity)
    }

    fun onClickAddDeadline(view: View)
    {
        val addDeadlineAddDeadlineIntent = Intent(this, AddDeadlineActivity::class.java)
        startActivity(addDeadlineAddDeadlineIntent)
    }

    fun onClickBackListDeadlines(view:View)
    {
        val backListDeadlinesIntent = Intent(this, StartActivity::class.java)
        startActivity(backListDeadlinesIntent)
    }
}