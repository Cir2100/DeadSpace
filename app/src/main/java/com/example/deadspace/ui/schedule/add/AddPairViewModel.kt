package com.example.deadspace.ui.schedule.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch

class AddPairViewModel(private val myPairDAO: MyPairDAO) : ViewModel()  {


    companion object {
        val FACTORY = singleArgViewModelFactory(::AddPairViewModel)
    }

    //TODO: this in constructor
    private val scheduleEditor = ScheduleEditor()

    fun addPair(
        group : String,
        weekDay : Int,
        typeOfWeek : Int,
        disc : String,
        type : String,
        less : Int,
        teachers : String,
        building : String,
        auditorium : String,
    ) {

        viewModelScope.launch {
            scheduleEditor.addPair(
                name = group,
                weekDay =  weekDay,
                less = less,
                week = typeOfWeek,
                type = (if (type.isNotEmpty()) type else "-"),
                disc = disc,
                teachers = (if (teachers.isNotEmpty()) teachers else "-"),
                groups = "-",
                build = (if (building.isNotEmpty()) building else "-"),
                room = (if (auditorium.isNotEmpty()) auditorium else "-")
            )
        }
    }
}