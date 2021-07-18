package com.example.deadspace.ui.deadlines.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.deadline.DeadlineEditor
import com.example.deadspace.data.deadline.getDeadlineEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddDeadlineViewModel : ViewModel() {

    private val deadlineEditor = getDeadlineEditor()

    fun onAddDeadline(title : String, discipline : String, lastDate : String) {
        viewModelScope.launch {
            deadlineEditor.addDeadline(
                title,
                discipline,
                lastDate.substring(0, 2) + "." + lastDate.substring(3, 5)
            )
        }
    }
}