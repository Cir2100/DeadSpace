package com.example.deadspace.ui.deadlines.main

import com.example.deadspace.R
import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.DeadlinesActivityBinding
import com.example.deadspace.databinding.StartActivityBinding
import com.example.deadspace.ui.deadlines.add.AddDeadlineActivity
import com.example.deadspace.ui.deadlines.add.AddDeadlineViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class DeadlinesActivity : AppCompatActivity() {

    private lateinit var viewModel: DeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Дедлайны"
        actionbar.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(DeadlineViewModel::class.java)

        val binding = DeadlinesActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //List
        val adapter = DeadlinesListAdapter(viewModel)
        binding.deadlineList.adapter = adapter
        binding.deadlineList.layoutManager = LinearLayoutManager(this)

        viewModel.myDeadlineList.observe(this) { value ->
        value?.let {
                adapter.updateItems(value)
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.sizeDeadlineList.observe(this) { value ->
            value.let { size ->
                binding.isNothingTextview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.isNothingImageview.visibility = if (size == 0) View.VISIBLE else View.GONE
                binding.deadlineList.visibility = if (size != 0) View.VISIBLE else View.GONE
            }
        }

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackBar.observe(this) { text ->
            text?.let {
                Snackbar.make(binding.deadlineRootLayout, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackBarShown()
            }
        }

        //Current date
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMMM")
        val formattedDate = formatter.format(date)
        binding.currentDateTextview.text = "Сегодня $formattedDate"

    }

    fun onClickAddDeadline(view: View)
    {
        val addDeadlineAddDeadlineIntent = Intent(this, AddDeadlineActivity::class.java)
        startActivity(addDeadlineAddDeadlineIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}