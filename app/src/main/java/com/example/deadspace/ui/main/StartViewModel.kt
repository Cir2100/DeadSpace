package com.example.deadspace.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.data.schedule.MyPairDao
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.data.schedule.ScheduleLoader
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel(private val myPairDao: MyPairDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::StartViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader(myPairDao)
    private val scheduleEditor = ScheduleEditor(myPairDao)


    /*private val _data = MutableLiveData<String>()
    val data : LiveData<String> = _data*/

    //val data = scheduleLoader.pairs

    val myPairList = scheduleLoader.pairs


    private val _isVisibleList = MutableLiveData<Boolean>()
    val isVisibleList : LiveData<Boolean> = _isVisibleList
//android:visibility="@{startViewModel.myPairList.size != 0}"

    var nameGroupListener : String = ""
    private var isUsers = false

    //TODO : use anti
    fun onChangeIsUser() {
        isUsers = !isUsers
    }

    fun onDelete() {

    }


    fun addPair() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val namePair = "Технология программирования"
        val type = "Л"
        val time = "11:30"
        val teachers = "Преподы"
        val groups = "Группы"
        val address = "Адресс"

        viewModelScope.launch {
            scheduleEditor.addPair(nameGroupListener, weekDay, time, typeOfWeek, type, namePair, teachers, groups,
                address)
        }
    }

}