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

    private val _toast : MutableLiveData<String?> = scheduleRepo.error
    val toast: LiveData<String?>
        get() = _toast

    private val _currentPair = MutableLiveData<PairData?>()
    val currentPair: LiveData<PairData?>
        get() = _currentPair

    init {
        viewModelScope.launch {
            try {
                _weekType.value = scheduleRepo.loadWeekType()

                loadCurrentPair()
            } catch (e : Throwable) {
                _toast.value = e.message
            }

        }

    }

    fun loadCurrentPair() {
        viewModelScope.launch {

            val date = Calendar.getInstance()
            val time = date.get(Calendar.HOUR_OF_DAY) * 60 + date.get(Calendar.MINUTE)
            val weekDay = date.get(Calendar.DAY_OF_WEEK) - 2

            _currentPair.value = scheduleRepo.loadCurrentPair(time, _weekType.value!!.toInt(), weekDay)
        }

    }

    fun onToastShown() {
        _toast.value = null
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}