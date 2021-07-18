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

    fun addPair(
        group : String,
        weekDay : Int,
        typeOfWeek : Int,
        title : String,
        type : String,
        number : String,
        teachers : String,
        building : String,
        auditorium : String,
    ) {

        viewModelScope.launch {
            scheduleEditor.addPair(
                group = group,
                day =  weekDay,
                time = number,
                week = typeOfWeek,
                type = (if (type.isNotEmpty()) type else "-"),
                name = title,
                teachers = (if (teachers.isNotEmpty()) teachers else "-"),
                groups = "-",
                address = (if (building.isNotEmpty()) building else "-") + "," + (if (auditorium.isNotEmpty()) auditorium else "-")
            )
        }
    }
}