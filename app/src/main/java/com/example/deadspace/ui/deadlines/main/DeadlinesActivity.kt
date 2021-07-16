package com.example.deadspace.ui.deadlines.main

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.ui.deadlines.add.AddDeadlineActivity
import com.example.deadspace.ui.schedule.main.ScheduleListAdapter
import com.example.deadspace.ui.schedule.main.ScheduleViewModel
import com.example.deadspace.ui.start.StartActivity


class DeadlinesActivity : AppCompatActivity() {

    private lateinit var viewModel: DeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deadlines_activity)

        val actionbar = supportActionBar
        actionbar!!.title = "Дедлайны"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            DeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(DeadlineViewModel::class.java)

        val deadlineList : RecyclerView = findViewById(R.id.deadline_list)

        //List
        val adapter = DeadlinesListAdapter(viewModel)
        deadlineList.adapter = adapter
        deadlineList.layoutManager = LinearLayoutManager(this)

        viewModel.myDeadlineList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }
    }

    fun onClickAddDeadline(view: View)
    {
        val addDeadlineAddDeadlineIntent = Intent(this, AddDeadlineActivity::class.java)
        startActivity(addDeadlineAddDeadlineIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}