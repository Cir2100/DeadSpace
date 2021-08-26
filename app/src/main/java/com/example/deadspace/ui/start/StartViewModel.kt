package com.example.deadspace.ui.start

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.PairData
import com.example.deadspace.data.database.getDatabase
import com.example.deadspace.data.schedule.getScheduleRepo
import kotlinx.coroutines.launch
import java.util.*

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel : ViewModel() {

    private val scheduleRepo = getScheduleRepo()

    private val _weekType = MutableLiveData(false)
    val weekType: LiveData<Boolean>
        get() = _weekType

    private val _snackBar : MutableLiveData<String?> = scheduleRepo.error
    val snackBar: LiveData<String?>
        get() = _snackBar

    private val _currentPair = MutableLiveData<PairData?>()
    val currentPair: LiveData<PairData?>
        get() = _currentPair

    init {
        viewModelScope.launch {
            try {
                _weekType.value = scheduleRepo.loadWeekType()

                loadCurrentPair()
            } catch (e : Throwable) {
                _snackBar.value = e.message
            }

        }

    }

    fun loadCurrentPair() {
        viewModelScope.launch {

            val date = Calendar.getInstance()
            val time = date.get(Calendar.HOUR_OF_DAY) * 60 + date.get(Calendar.MINUTE)

            _currentPair.value = scheduleRepo.loadCurrentPair(time)
        }

    }

    fun onSnackBarShown() {
        _snackBar.value = null
    }

}