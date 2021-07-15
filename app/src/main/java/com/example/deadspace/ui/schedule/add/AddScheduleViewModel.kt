package com.example.deadspace.ui.schedule.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch

class AddScheduleViewModel(private val myPairDAO: MyPairDAO) : ViewModel()  {


    companion object {
        val FACTORY = singleArgViewModelFactory(::AddScheduleViewModel)
    }

    //TODO: this in constructor
    private val scheduleEditor = ScheduleEditor(myPairDAO)

    private val nameGroupListener = "1942"

    fun addPair() {
        // TODO: users input
        val weekDay = 1
        val typeOfWeek = 1
        val namePair = "TP"
        val type = "Л"
        val time = "1"
        val teachers = "Преподы"
        val groups = "Группы"
        val address = "Адресс"

        viewModelScope.launch {
            Log.i(this.javaClass.simpleName, "Start add")
            scheduleEditor.addPair(
                group = nameGroupListener,
                day =  weekDay,
                time = time,
                week = typeOfWeek,
                type = type,
                name = namePair,
                teachers = teachers,
                groups = groups,
                address = address)
        }
    }
}