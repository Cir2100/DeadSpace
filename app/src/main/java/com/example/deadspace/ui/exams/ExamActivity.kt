package com.example.deadspace.ui.exams

import com.example.deadspace.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ExamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exam_activity)

        val actionbar = supportActionBar
        actionbar!!.title = "Расписание сессии"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}