package com.example.deadspace.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deadspace.R
import com.example.deadspace.schedule.sheadule
import com.example.deadspace.view.ui.main.StartFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, StartFragment.newInstance())
                .commitNow()
        }
    }
}