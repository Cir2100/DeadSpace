package com.example.deadspace.ui.schedule.add

import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.AddScheduleActivityBinding
import com.example.deadspace.ui.schedule.main.ScheduleActivity

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var viewModel: AddScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = getDatabase(this)

        viewModel = ViewModelProvider(
            this,
            AddScheduleViewModel.FACTORY(database.myPairDAO)
        ).get(AddScheduleViewModel::class.java)

        val binding : AddScheduleActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_schedule_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.addPairButton.setOnClickListener {
            viewModel.addPair()
            onClickBackAddRasp(binding.closeButton)
        }

    }

    fun onClickBackAddRasp(view:View)
    {
        val backAddRaspIntent = Intent(this, ScheduleActivity::class.java)
        startActivity(backAddRaspIntent)
    }

    //TODO : use onSavedInstanceState or savedStateHandle
}