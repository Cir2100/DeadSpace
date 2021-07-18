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


    fun onDeleteDeadline(id : Int) {
        viewModelScope.launch {
            deadlineEditor.deleteDeadline(id)
        }
    }

    fun onDoneChange (id : Int) {
        viewModelScope.launch {
            deadlineEditor.changeDeadline(id)
        }
    }
}