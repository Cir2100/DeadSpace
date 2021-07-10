package com.example.deadspace.view

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity



class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
    }
    fun onMyButtonClick(view:View)
    {
        val DeadlinesActivityIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(DeadlinesActivityIntent)

    }
}