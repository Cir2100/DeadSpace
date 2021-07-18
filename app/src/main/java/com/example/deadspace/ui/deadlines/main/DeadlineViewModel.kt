package com.example.deadspace.ui.deadlines.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _snackBar : MutableLiveData<String?> = deadlineEditor.error
    val snackBar: LiveData<String?>
        get() = _snackBar

    fun onSnackBarShown() {
        _snackBar.value = null
    }


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