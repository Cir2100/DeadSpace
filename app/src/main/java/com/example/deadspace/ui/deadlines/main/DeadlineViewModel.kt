package com.example.deadspace.ui.deadlines.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.deadline.DeadlineEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.time.LocalDate

class DeadlineViewModel(private val myDeadlinesDAO: MyDeadlinesDAO) : ViewModel() {

    private val deadlineEditor = DeadlineEditor(myDeadlinesDAO)

    val myDeadlineList = deadlineEditor.deadlines

    companion object {
        val FACTORY = singleArgViewModelFactory(::DeadlineViewModel)
    }

    fun onDeleteDeadline(title : String, discipline : String, lastDate : String) {
        viewModelScope.launch {
            deadlineEditor.deleteDeadline(title, discipline, lastDate)
        }
    }
}