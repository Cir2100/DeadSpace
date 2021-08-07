package com.example.deadspace.ui.start

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.data.schedule.getScheduleRepo
import kotlinx.coroutines.launch

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel : ViewModel() {

    private val scheduleRepo = getScheduleRepo()

    private val _weekType = MutableLiveData<Boolean>(false)
    val weekType: LiveData<Boolean>
        get() = _weekType

    private val _snackBar : MutableLiveData<String?> = scheduleRepo.error
    val snackBar: LiveData<String?>
        get() = _snackBar

    init {
        viewModelScope.launch {
            try {
                _weekType.value = scheduleRepo.loadWeekType()
            } catch (e : Throwable) {
                _snackBar.value = e.message
            }

        }

    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

}