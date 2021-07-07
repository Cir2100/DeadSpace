package com.example.deadspace.view

<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
=======
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
>>>>>>> origin/design
import com.example.deadspace.R
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