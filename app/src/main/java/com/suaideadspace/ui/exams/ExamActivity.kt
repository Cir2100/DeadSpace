package com.suaideadspace.ui.exams

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.suaideadspace.R
import com.suaideadspace.databinding.ExamActivityBinding
import java.util.*

class ExamActivity : AppCompatActivity() {

    private lateinit var viewModel: ExamViewModel

    private lateinit var prefs: SharedPreferences

    private lateinit var mAdapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Расписание сессии"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val binding = ExamActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ExamViewModel::class.java)
        loadPreferences()

        //Current date
        val date = Calendar.getInstance()
        val formattedDate = date.get(Calendar.DAY_OF_MONTH).toString() + " " +
                date.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, Locale.getDefault())
        binding.currentDateTextview.text = resources.getString(
            R.string.current_date_text, formattedDate )

        //List
        val adapter = ExamListAdapter()
        binding.examList.adapter = adapter
        binding.examList.layoutManager = LinearLayoutManager(this)

        viewModel.listExams.observe(this) { value ->
            value?.let {
                adapter.updateItems(value)
            }
        }

        //current group
        binding.nameGroupInput.setQuery(viewModel.currentGroup, false)

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
                    return false
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

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.toast.observe(this) { text ->
            text?.let {
                Toast.makeText(this@ExamActivity, text,
                    Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onPause() {
        super.onPause()

        // Запоминаем данные
        val editor = prefs.edit()
        editor.putString("APP_PREFERENCES_GROUP_EXAMS", viewModel.currentGroup)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
    }

    private fun loadPreferences() {
        if(prefs.contains("APP_PREFERENCES_GROUP_EXAMS")){
            viewModel.currentGroup = prefs.getString("APP_PREFERENCES_GROUP_EXAMS", "").toString()
        }
    }

    private fun updateCursor(text : String) : Cursor {
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID, "Name"))
        for (i in viewModel.querySuggestions.indices) {
            if (viewModel.querySuggestions[i].Name.lowercase().startsWith(text.lowercase()))
                cursor.addRow(arrayOf<Any>(i, viewModel.querySuggestions[i].Name))
        }
        return cursor
    }

}