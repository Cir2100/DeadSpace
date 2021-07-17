package com.example.deadspace.ui.deadlines.main

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.DeadlinesActivityBinding
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.deadlines.add.AddDeadlineActivity
import com.example.deadspace.ui.schedule.main.ScheduleListAdapter
import com.example.deadspace.ui.schedule.main.ScheduleViewModel
import com.example.deadspace.ui.start.StartActivity
import java.text.SimpleDateFormat
import java.util.*


class DeadlinesActivity : AppCompatActivity() {

    private lateinit var viewModel: DeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Дедлайны"
        actionbar.setDisplayHomeAsUpEnabled(true)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            DeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(DeadlineViewModel::class.java)

        val binding : DeadlinesActivityBinding = DataBindingUtil.setContentView(this, R.layout.deadlines_activity)
        binding.lifecycleOwner = this

        //List
        val adapter = DeadlinesListAdapter(viewModel)
        binding.deadlineList.adapter = adapter
        binding.deadlineList.layoutManager = LinearLayoutManager(this)

        viewModel.myDeadlineList.observe(this) { value ->
        value?.let {
                adapter.updateItems(value)
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.sizeDeadlineList.observe(this) { value ->
            value.let { size ->
                binding.isNothingTextview.visibility = if (size == 0) View.VISIBLE else View.GONE
            }
        }

        //Current date
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMMM")
        val formatedDate = formatter.format(date)
        binding.currentDateTextview.text = "Сегодня $formatedDate"

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