package com.example.deadspace.view

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity



class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
    }
    fun ClickListDeadlines(view:View)
    {
        val DeadlinesActivityIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(DeadlinesActivityIntent)
    }
    fun ClickTest(view:View)
    {
        val TestIntent = Intent(this, ExamActivity::class.java)
        startActivity(TestIntent)
    }
    fun ClickPairs(view: View)
    {
        val PairsIntent = Intent(this, RaspActivity::class.java)
        startActivity(PairsIntent)
    }
}