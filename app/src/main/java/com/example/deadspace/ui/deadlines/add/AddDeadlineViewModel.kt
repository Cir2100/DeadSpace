package com.example.deadspace.ui.deadlines.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.deadline.DeadlineEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddDeadlineViewModel(private val myDeadlinesDAO: MyDeadlinesDAO) : ViewModel() {

    private val deadlineEditor = DeadlineEditor(myDeadlinesDAO)


    companion object {
        val FACTORY = singleArgViewModelFactory(::AddDeadlineViewModel)
    }

    fun onAddDeadline(title : String, discipline : String, lastDate : String) {
        viewModelScope.launch {
            deadlineEditor.addDeadline(title, discipline, lastDate)
        }
    }
}