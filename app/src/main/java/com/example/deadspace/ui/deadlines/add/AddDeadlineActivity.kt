package com.example.deadspace.ui.deadlines.add

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.AddDeadlineActivityBinding
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.deadlines.main.DeadlineViewModel
import com.example.deadspace.ui.deadlines.main.DeadlinesActivity

class AddDeadlineActivity : AppCompatActivity() {

    private lateinit var viewModel: AddDeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_deadline_activity)
        val actionbar = supportActionBar
        actionbar!!.title = "Добавление дедлайна"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            AddDeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(AddDeadlineViewModel::class.java)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}