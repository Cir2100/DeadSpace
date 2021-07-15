package com.example.deadspace.ui.deadlines.add

import android.content.Context
import com.example.deadspace.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.AddDeadlineActivityBinding
import com.example.deadspace.databinding.ScheduleActivityBinding
import com.example.deadspace.ui.deadlines.main.DeadlineViewModel
import com.example.deadspace.ui.deadlines.main.DeadlinesActivity

class AddDeadlineActivity : AppCompatActivity() {

    private lateinit var viewModel: AddDeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            AddDeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(AddDeadlineViewModel::class.java)

        val binding : AddDeadlineActivityBinding = DataBindingUtil.setContentView(this, R.layout.add_deadline_activity)

        binding.addDeadlineButton.setOnClickListener {
            viewModel.onAddDeadline(
                binding.deadlineTittleInput.text.toString(),
                binding.deadlineDisciplineInput.text.toString(),
                binding.deadlineDateInput.text.toString()
            )
            onClickBackAddDeadlines(binding.closeButton)
        }

        binding.deadlineDateInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    //скрываем клавиатуру
                    var imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    binding.deadlineDateInput.isCursorVisible = false

                    return true
                }
                return false
            }
        })
    }

    fun onClickBackAddDeadlines(view:View)
    {
        val backAddDeadlinesIntent = Intent(this, DeadlinesActivity::class.java)
        startActivity(backAddDeadlinesIntent)
    }
}