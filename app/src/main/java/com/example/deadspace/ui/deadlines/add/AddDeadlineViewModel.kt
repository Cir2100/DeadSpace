package com.example.deadspace.ui.deadlines.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.deadline.getDeadlineRepo
import kotlinx.coroutines.launch

class AddDeadlineViewModel : ViewModel() {

    private val deadlineRepo = getDeadlineRepo()

    fun onAddDeadline(title : String, discipline : String, lastDate : String) {
        viewModelScope.launch {
            deadlineRepo.addDeadline(
                title,
                discipline,
                lastDate.substring(0, 2) + "." + lastDate.substring(3, 5)
            )
        }
    }
}