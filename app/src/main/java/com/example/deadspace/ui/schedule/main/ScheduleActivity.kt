package com.example.deadspace.ui.schedule.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.R
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.schedule.add.AddPairActivity
import java.text.SimpleDateFormat
import java.util.*
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.widget.CursorAdapter
import android.widget.FilterQueryProvider
import com.google.android.material.snackbar.Snackbar


class ScheduleActivity : AppCompatActivity() {

    //TODO : maybe use savedStateHandle

    //TODO : use this normal viewModel or not?
    private lateinit var prefs: SharedPreferences

    private lateinit var viewModel: ScheduleViewModel
    private lateinit var binding : ScheduleActivityBinding

    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Расписание занятий"
        actionbar.setDisplayHomeAsUpEnabled(true)


        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        loadPreferences()

        binding = DataBindingUtil.setContentView(this, R.layout.schedule_activity)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackBar.observe(this) { text ->
            text?.let {
                Snackbar.make(binding.scheduleRootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackBarShown()
            }
        }

        //current group
        binding.nameGroupInput.setQuery(viewModel.currentGroup, true)

        //Current date
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMMM")
        val formattedDate = formatter.format(date)
        binding.currentDateTextview.text = formattedDate

        //List
        val adapter = ScheduleListAdapter(viewModel)
        binding.pairList.adapter = adapter
        binding.pairList.layoutManager = LinearLayoutManager(this)

        viewModel.myPairList.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }
        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            arrayOf("Name"),
            intArrayOf(android.R.id.text1),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        mAdapter.filterQueryProvider =
            FilterQueryProvider { constraint -> updateCursor(constraint as String) }

        binding.nameGroupInput.suggestionsAdapter = mAdapter

        binding.nameGroupInput.setOnSuggestionListener(
            object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val item = binding.nameGroupInput.suggestionsAdapter.getItem(position) as Cursor
                    val text = item.getString(item.getColumnIndex("Name"))
                    binding.nameGroupInput.setQuery(text, true)
                    return true
                }
            }
        )

        binding.nameGroupInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearch(query)
                return false
            }

        })

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                binding.loadSpinner.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        viewModel.pairsCount.observe(this) {
                value ->
            value.let { size ->
                binding.isNothingTextview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.isNothingImageview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.pairList.visibility = if (size != 0) View.VISIBLE else View.GONE
            }
        }

        viewModel.weekType.observe(this) { value ->
            value.let { type ->
                binding.typeOfWeek.text = if (type == 1) resources.getString(R.string.upperWeek)
                else resources.getString(R.string.lowerWeek)
                binding.typeOfWeek.background = if (type == 1) resources.getDrawable(R.drawable.oval_button_red, theme)
                else resources.getDrawable(R.drawable.oval_button_blue, theme)
            }
        }

        binding.isUsersSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onChangeIsUser(isChecked)
        }

        binding.isUsersSwitcher.isChecked = viewModel.isUsers

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
        viewModel.loadDaySchedule()  //TODO don't use this
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

    fun onClickAddRasp(view: View)
    {
        val addScheduleIntent = Intent(this, AddPairActivity::class.java)
        addScheduleIntent.putExtra("group", viewModel.currentGroup)
        addScheduleIntent.putExtra("weekDay", viewModel.weekDay)
        addScheduleIntent.putExtra("weekType", viewModel.weekType.value!!)
        startActivity(addScheduleIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateCursor(text : String) : Cursor {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "Name"))
        for (i in viewModel._querySuggestions.indices) {
            if (viewModel._querySuggestions[i].Name.lowercase().startsWith(text.lowercase()))
                cursor.addRow(arrayOf<Any>(i, viewModel._querySuggestions[i].Name))
        }
        return cursor
    }

}