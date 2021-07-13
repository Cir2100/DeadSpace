package com.example.deadspace.ui.schedule

import android.content.Intent
import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.data.schedule.getDatabase
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.main.StartActivity
import com.example.deadspace.ui.main.StartViewModel

class ScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            ScheduleViewModel.FACTORY(database.myPairDao)
        ).get(ScheduleViewModel::class.java)

        val binding : ScheduleActivityBinding = DataBindingUtil.setContentView(this, R.layout.schedule_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //List
        val adapter = ScheduleListAdapter(viewModel)
        binding.pairList.adapter = adapter
        binding.pairList.layoutManager = LinearLayoutManager(this)

        viewModel.myPairList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }

    }

    fun onClickBackListRasp(view: View)
    {
        val backListRaspIntent = Intent(this, StartActivity::class.java)
        startActivity(backListRaspIntent)
    }

    fun onClickAddRasp(view: View)
    {
        val addRaspIntent = Intent(this, AddRaspActivity::class.java)
        startActivity(addRaspIntent)
    }

}