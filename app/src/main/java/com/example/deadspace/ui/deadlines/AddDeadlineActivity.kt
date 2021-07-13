package com.example.deadspace.ui.deadlines

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class AddDeadlineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_deadline_activity)
    }

    fun onClickBackAddDeadlines(view:View)
    {
        val backAddDeadlinesIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(backAddDeadlinesIntent)
    }
}