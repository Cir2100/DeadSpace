package com.example.deadspace.ui.deadlines.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.databinding.AddDeadlineActivityBinding
import com.example.deadspace.ui.deadlines.main.DeadlinesActivity
import java.text.SimpleDateFormat
import java.util.*

//TODO : use strings in errors and check errors normal 1 and 2

class AddDeadlineActivity : AppCompatActivity() {

    private lateinit var viewModel: AddDeadlineViewModel
    private lateinit var binding : AddDeadlineActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Добавление дедлайна"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val database = getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            AddDeadlineViewModel.FACTORY(database.myDeadlinesDAO)
        ).get(AddDeadlineViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.add_deadline_activity)

        setupListeners()

        binding.addDeadlineButton.setOnClickListener {
            if (isValidate()) {
                viewModel.onAddDeadline(
                    binding.deadlineTitleInput.text.toString(),
                    binding.deadlineDisciplineInput.text.toString(),
                    binding.deadlineDateInput.text.toString()
                )
                onBackPressed()
            }
        }

        binding.deadlineDateInput.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    //скрываем клавиатуру
                    var imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    binding.deadlineDateInput.isCursorVisible = false
                    //TODO : auto click addButton
                    return true
                }
                return false
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupListeners() {
        binding.deadlineTitleInput.addTextChangedListener(TextFieldValidation(binding.deadlineTitleInput))
        binding.deadlineDisciplineInput.addTextChangedListener(TextFieldValidation(binding.deadlineDisciplineInput))
        binding.deadlineDateInput.addTextChangedListener(TextFieldValidation(binding.deadlineDateInput))
    }

    private fun isValidate(): Boolean =
        validateTitle() && validateDiscipline() && validateLastDate()

    private fun validateTitle() : Boolean {
        if (binding.deadlineTitleInput.text.toString().trim().isEmpty()) {
            binding.deadlineTitleInputLayout.error = "Название не должно быть пустым"
            binding.deadlineTitleInput.requestFocus()
            return false
        } else {
            binding.deadlineTitleInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateDiscipline() : Boolean {
        if (binding.deadlineDisciplineInput.text.toString().trim().isEmpty()) {
            binding.deadlineDisciplineInputLayout.error = "Название дисциплины не должно быть пустым"
            binding.deadlineDisciplineInput.requestFocus()
            return false
        } else {
            binding.deadlineDisciplineInputLayout.isErrorEnabled = false
        }
        return true
    }

    private fun validateLastDate() : Boolean {
        val input = binding.deadlineDateInput.text.toString().trim()
        when {
            (input.isEmpty()) -> {
                binding.deadlineDateInputLayout.error = "Дата должна быть указана"
                binding.deadlineDateInput.requestFocus()
                return false
            }
            (input.length > 1) -> {
               return checkDate(input)
            }
            else -> {
                binding.deadlineDateInputLayout.isErrorEnabled = false
            }
        }
        return true
    }

    private fun checkDate(date : String) : Boolean{
        if (date.length == 5) {
            val day = date.substring(0,2).toInt()
            val month= date.substring(3,5).toInt()

            if (month < 1 || month > 12) {
                binding.deadlineDateInputLayout.error = "Косяк в месяце"
                return false
            }

            val date = Calendar.getInstance()
            date.set(Calendar.MONTH, month - 1)

            if (day < 1 || day > date.getActualMaximum(Calendar.DATE)) {
                binding.deadlineDateInputLayout.error = "Косяк в дне"
                return false
            }
        } else {
            binding.deadlineDateInputLayout.error = "Неверный формат даты"
            return false
        }
        binding.deadlineDateInputLayout.isErrorEnabled = false
        return true
    }

    /**
     * applying text watcher on each text field
     */
    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.deadline_title_input -> {
                    validateTitle()
                }
                R.id.deadline_discipline_input -> {
                    validateDiscipline()
                }
                R.id.deadline_date_input -> {
                    validateLastDate()
                }
            }
        }
    }

}