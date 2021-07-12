package com.example.deadspace.ui.exams

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.example.deadspace.ui.main.StartActivity




class ExamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)
    }

    fun ClickBackExam(view:View)
    {
        val BackTestIntent = Intent(this, StartActivity::class.java)
        startActivity(BackTestIntent)
    }
}