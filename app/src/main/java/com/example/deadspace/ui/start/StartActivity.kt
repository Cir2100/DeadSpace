package com.example.deadspace.ui.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.databinding.StartActivityBinding
import com.example.deadspace.ui.deadlines.main.DeadlinesActivity
import com.example.deadspace.ui.exams.ExamActivity
import com.example.deadspace.ui.schedule.main.ScheduleActivity
import java.util.*

class StartActivity : AppCompatActivity() {

    //private lateinit var viewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = StartActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Навигация"

        val date = Calendar.getInstance()
        binding.currentDateDay.text = date.get(Calendar.DATE).toString()
        binding.currentDateMonth.text = date.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.getDefault())
        binding.currentWeekday.text = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.getDefault())


        /*val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            StartViewModel.FACTORY(database.myPairDAO)
        ).get(StartViewModel::class.java)*/


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