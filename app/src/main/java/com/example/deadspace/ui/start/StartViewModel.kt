package com.example.deadspace.ui.start

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.data.suai.*
import kotlinx.coroutines.launch

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel : ViewModel() {

    private val _weekType = MutableLiveData<Boolean>()
    val weekType : LiveData<Boolean>
        get() = _weekType

    init {
        viewModelScope.launch {
            try {
            _weekType.value = SUAIScheduleLoader2.getSemInfo().IsWeekUp

            } catch (e : Throwable) {
                Log.e("dsf", e.toString())
            }

        }

    }

}