package com.example.deadspace.ui.deadlines.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.deadline.DeadlineEditor
import com.example.deadspace.data.deadline.getDeadlineEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch

class DeadlineViewModel : ViewModel() {

    private val deadlineEditor = getDeadlineEditor()

    val myDeadlineList = deadlineEditor.deadlines

    val sizeDeadlineList = deadlineEditor.countDeadlines


    fun onDeleteDeadline(title : String, discipline : String, lastDate : String) {
        viewModelScope.launch {
            deadlineEditor.deleteDeadline(title, discipline, lastDate)
        }
    }

    fun onDoneChange (title : String, discipline : String, lastDate : String, isDone : Boolean) {
        viewModelScope.launch {
            deadlineEditor.changeDeadline(title, discipline, lastDate, isDone)
        }
    }
}