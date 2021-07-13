package com.example.deadspace.ui.schedule

import android.content.Intent
import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.data.schedule.getDatabase
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.databinding.StartActivityBinding
import com.example.deadspace.ui.main.StartActivity
import com.example.deadspace.ui.main.StartViewModel

class ScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ScheduleActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            ScheduleViewModel.FACTORY(database.myPairDao)
        ).get(ScheduleViewModel::class.java)

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