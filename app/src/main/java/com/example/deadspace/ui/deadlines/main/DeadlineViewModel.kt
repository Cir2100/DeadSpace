package com.example.deadspace.ui.deadlines.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.deadline.getDeadlineRepo
import kotlinx.coroutines.launch

class DeadlineViewModel : ViewModel() {

    //private val deadlineEditor = getDeadlineEditor()
    private val deadlineRepo = getDeadlineRepo()

    val myDeadlineList = deadlineRepo.deadlines

    val sizeDeadlineList = deadlineRepo.countDeadlines

    private val _snackBar : MutableLiveData<String?> = deadlineRepo.error
    val snackBar: LiveData<String?>
        get() = _snackBar

    fun onSnackBarShown() {
        _snackBar.value = null
    }


    fun onDeleteDeadline(id : Int) {
        viewModelScope.launch {
            deadlineRepo.deleteDeadline(id)
        }
    }

    fun onDoneChange (id : Int) {
        viewModelScope.launch {
            deadlineRepo.changeDeadline(id)
        }
    }
}