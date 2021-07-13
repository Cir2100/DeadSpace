package com.example.deadspace.ui.schedule.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.data.schedule.getDatabase
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.main.StartActivity
import com.example.deadspace.ui.schedule.add.AddScheduleActivity
import java.text.SimpleDateFormat
import java.util.*

class ScheduleActivity : AppCompatActivity() {

    //TODO : use this normal viewModel or not?
    private lateinit var prefs: SharedPreferences

    private lateinit var viewModel: ScheduleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs =
            getSharedPreferences("settings", Context.MODE_PRIVATE)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            ScheduleViewModel.FACTORY(database.myPairDao)
        ).get(ScheduleViewModel::class.java)
        loadPreferences()

        val binding : ScheduleActivityBinding = DataBindingUtil.setContentView(this, R.layout.schedule_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //Current date
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMMM")
        val formatedDate = formatter.format(date)
        binding.currentDateTextview.text = formatedDate

        //List
        val adapter = ScheduleListAdapter(viewModel)
        binding.pairList.adapter = adapter
        binding.pairList.layoutManager = LinearLayoutManager(this)

        binding.nameGroupInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearch(query)
                return false
            }

        })

        viewModel.myPairList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                binding.loadSpinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.currentGroupLive.observe(this) { value ->
            value.let { show ->
                binding.costil.text = value
            }
        }

        //TODO : delete this

    }

    override fun onPause() {
        super.onPause()

        // Запоминаем данные
        val editor = prefs.edit()
        editor.putString("APP_PREFERENCES_GROUP", viewModel.currentGroup)
        editor.putBoolean("APP_PREFERENCES_IS_USER", viewModel.isUsers)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
    }

    private fun loadPreferences() {
        if(prefs.contains("APP_PREFERENCES_GROUP")){
            viewModel.currentGroup = prefs.getString("APP_PREFERENCES_GROUP", "").toString()
            viewModel.currentGroupLive.postValue(viewModel.currentGroup)
        }
        if(prefs.contains("APP_PREFERENCES_IS_USER")){
            viewModel.isUsers = prefs.getBoolean("APP_PREFERENCES_IS_USER", false)
        }
    }

    fun onClickBackListRasp(view: View)
    {
        val backListRaspIntent = Intent(this, StartActivity::class.java)
        startActivity(backListRaspIntent)
    }

    fun onClickAddRasp(view: View)
    {
        val addRaspIntent = Intent(this, AddScheduleActivity::class.java)
        startActivity(addRaspIntent)
    }

}