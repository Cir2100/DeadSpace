package com.example.deadspace.view.ui.main

import androidx.lifecycle.*
import com.example.deadspace.schedule.MyPairDao
import com.example.deadspace.schedule.ScheduleLoader
import kotlinx.coroutines.launch

//TODO: use Hilt
//@HiltViewModel
class StartViewModel(private val myPairDao: MyPairDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::StartViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader()


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
            val result = scheduleLoader.loadSchedule(name = name, isUsers = isUsers)

            //myPairDao.insertAll(result)
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