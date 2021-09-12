package com.suaideadspace.ui.deadlines.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suaideadspace.data.deadline.getDeadlineRepo
import kotlinx.coroutines.launch

class DeadlineViewModel : ViewModel() {

    private val deadlineRepo = getDeadlineRepo()

    val myDeadlineList = deadlineRepo.deadlines

    val sizeDeadlineList = deadlineRepo.countDeadlines

    private val _toast : MutableLiveData<String?> = deadlineRepo.error
    val toast: LiveData<String?>
        get() = _toast

    fun onToastBarShown() {
        _toast.value = null
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