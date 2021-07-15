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

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            AddDeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(AddDeadlineViewModel::class.java)

        val binding : AddDeadlineActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_deadline_activity)

        binding.addDeadlineButton.setOnClickListener {
            viewModel.onAddDeadline(
                binding.deadlineTittleInput.text.toString(),
                binding.deadlineDisciplineInput.text.toString(),
                binding.deadlineDateInput.text.toString()
            )
            onClickBackAddDeadlines(binding.closeButton)
        }
    }

    fun onClickBackAddDeadlines(view:View)
    {
        val backAddDeadlinesIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(backAddDeadlinesIntent)
    }
}