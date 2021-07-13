package com.example.deadspace.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
import com.example.deadspace.data.schedule.getDatabase
import com.example.deadspace.ui.deadlines.DeadlinesActivity
import com.example.deadspace.ui.exams.ExamActivity
import com.example.deadspace.ui.schedule.main.ScheduleActivity

class StartActivity : AppCompatActivity() {

    //TODO : use view binding or data binding

    private lateinit var viewModel: StartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val binding = StartActivityBinding.inflate(layoutInflater)

        setContentView(R.layout.start_activity)


        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            StartViewModel.FACTORY(database.myPairDao)
        ).get(StartViewModel::class.java)


        /*//List
        val adapter = PairListAdapter(viewModel)
        binding.pairList.adapter = adapter
        binding.pairList.layoutManager = LinearLayoutManager(this)

        viewModel.myPairList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }*/

    }

    fun onClickListDeadlines(view: View) {
        val deadlinesActivityIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(deadlinesActivityIntent)
    }

    fun onClickTest(view: View) {
        val testIntent = Intent(this, ExamActivity::class.java)
        startActivity(testIntent)
    }

    fun onClickPairs(view: View) {
        val pairsIntent = Intent(this, ScheduleActivity::class.java)
        startActivity(pairsIntent)
    }

    //TODO : use onSavedInstanceState or savedStateHandle
}