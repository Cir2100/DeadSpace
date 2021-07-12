package com.example.deadspace.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.R
import com.example.deadspace.databinding.StartActivityBinding
import com.example.deadspace.data.schedule.getDatabase
import com.example.deadspace.ui.deadlines.DeadlinesActivity
import com.example.deadspace.ui.exams.ExamActivity
import com.example.deadspace.ui.schedule.RaspActivity

class StartActivity : AppCompatActivity() {

    //TODO : use view binding or data binding

    private lateinit var viewModel  : StartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = StartActivityBinding.inflate(layoutInflater)

        setContentView(binding.root)


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

    fun ClickListDeadlines(view: View)
    {
        val DeadlinesActivityIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(DeadlinesActivityIntent)
    }
    fun ClickTest(view: View)
    {
        val TestIntent = Intent(this, ExamActivity::class.java)
        startActivity(TestIntent)
    }
    fun ClickPairs(view: View)
    {
        val PairsIntent = Intent(this, RaspActivity::class.java)
        startActivity(PairsIntent)
    }

    //TODO : use onSavedInstanceState or savedStateHandle
}