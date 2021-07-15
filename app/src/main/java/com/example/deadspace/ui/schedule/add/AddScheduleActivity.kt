package com.example.deadspace.ui.schedule.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
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
            viewModel.addPair(
                group = intent.extras?.getString("group")!!,
                weekDay = intent.extras?.getInt("weekDay")!!,
                typeOfWeek = intent.extras?.getInt("weekType")!!,
                title = binding.scheduleTitleInput.text.toString(),
                type = binding.scheduleTypeInput.text.toString(),
                number = binding.scheduleNumberInput.text.toString(),
                teachers = binding.scheduleTeacherInput.text.toString(),
                building = binding.scheduleBuildingInput.text.toString(),
                auditorium = binding.scheduleAuditoriumInput.text.toString(),
            )
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