package com.example.deadspace.view.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.schedule.ScheduleLoader
import kotlinx.coroutines.launch

//@HiltViewModel
class StartViewModel //@Inject constructor
    : ViewModel() {

    private val _data = MutableLiveData<String>()
    val data : LiveData<String> = _data

    fun onSearch() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val name = "1942"
        val isUsers = true

        viewModelScope.launch {

            //_data.value = ScheduleLoader().loadSchedule("1942", true)
            val result = ScheduleLoader().loadSchedule(name = name, isUsers = isUsers)

            //TODO : use interface
            /*for (pair in result[weekDay]) {
                if (pair.week == 2 || pair.week == typeOfWeek) {
                    Log.e(ContentValues.TAG, pair.time)
                    Log.e(ContentValues.TAG, pair.week.toString())
                    Log.e(ContentValues.TAG, pair.type)
                    Log.e(ContentValues.TAG, pair.name)
                    Log.e(ContentValues.TAG, pair.teacher)
                    Log.e(ContentValues.TAG, pair.groups)
                    Log.e(ContentValues.TAG, pair.address)
                }
            }*/
        }
    }

}