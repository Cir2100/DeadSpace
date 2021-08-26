package com.example.deadspace.ui.schedule.add

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.deadspace.R
import com.example.deadspace.databinding.AddPairActivityBinding

class AddPairActivity : AppCompatActivity() {

    private lateinit var viewModel: AddPairViewModel
    private lateinit var binding : AddPairActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionbar = supportActionBar
        actionbar!!.title = "Добавление занятия"
        actionbar.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(AddPairViewModel::class.java)

        binding = AddPairActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()

        binding.addPairButton.setOnClickListener {
            if (isValidate()) {
                viewModel.addPair(
                    group = intent.extras?.getString("group")!!,
                    weekDay = intent.extras?.getInt("weekDay")!!,
                    typeOfWeek = intent.extras?.getInt("weekType")!!,
                    disc = binding.scheduleTitleInput.text.toString(),
                    type = binding.scheduleTypeInput.text.toString(),
                    less = binding.scheduleNumberInput.text.toString().toInt(),
                    teachers = binding.scheduleTeacherInput.text.toString(),
                    building = binding.scheduleBuildingInput.text.toString(),
                    auditorium = binding.scheduleAuditoriumInput.text.toString(),
                )
            onBackPressed()
            }
        }

        binding.scheduleTeacherInput.setOnKeyListener(object : View.OnKeyListener {
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
                    binding.scheduleTeacherInput.isCursorVisible = false
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
        binding.scheduleTitleInput.addTextChangedListener(TextFieldValidation(binding.scheduleTitleInput))
        binding.scheduleNumberInput.addTextChangedListener(TextFieldValidation(binding.scheduleNumberInput))
    }

    private fun isValidate(): Boolean =
        validateNumber() && validateTitle()


    private fun validateNumber() : Boolean {
        val input  = binding.scheduleNumberInput.text.toString()
        when {
            input.trim().isEmpty() -> {
                binding.scheduleNumberInputLayout.error = "Номер не должен быть пуст"
                binding.scheduleNumberInput.requestFocus()
                return false
            }
            input.toInt() < 1 || input.toInt() > 9 -> {
                binding.scheduleNumberInputLayout.error = "Номер пары от 1 до 9"
                binding.scheduleNumberInput.requestFocus()
                return false
            }
            else -> {
                binding.scheduleNumberInputLayout.isErrorEnabled = false
            }
        }
        return true
    }

    private fun validateTitle() : Boolean {
        if (binding.scheduleTitleInput.text.toString().trim().isEmpty()) {
            binding.scheduleTitleInputLayout.error = "Название не должно быть пустым"
            binding.scheduleTitleInput.requestFocus()
            return false
        } else {
            binding.scheduleTitleInputLayout.isErrorEnabled = false
        }
        return true
    }

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.schedule_number_input -> {
                    validateNumber()
                }
                R.id.schedule_title_input -> {
                    validateTitle()
                }
            }
        }
    }

    //TODO : use onSavedInstanceState or savedStateHandle
}