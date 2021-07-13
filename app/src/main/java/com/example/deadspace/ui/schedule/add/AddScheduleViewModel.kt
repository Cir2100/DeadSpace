package com.example.deadspace.ui.schedule.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.schedule.MyPairDao
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch

class AddScheduleViewModel(private val myPairDao: MyPairDao) : ViewModel()  {


    companion object {
        val FACTORY = singleArgViewModelFactory(::AddScheduleViewModel)
    }

    //TODO: this in constructor
    private val scheduleEditor = ScheduleEditor(myPairDao)

    private val nameGroupListener = "1942"

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
            Log.i(this.javaClass.simpleName, "Start add")
            scheduleEditor.addPair(nameGroupListener, weekDay, time, typeOfWeek, type, namePair, teachers, groups,
                address)
        }
    }
}