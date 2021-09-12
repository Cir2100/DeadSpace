package com.suaideadspace.ui.schedule.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suaideadspace.data.schedule.getScheduleRepo
import kotlinx.coroutines.launch

class AddPairViewModel : ViewModel()  {

    //TODO: this in constructor
    private val scheduleRepo = getScheduleRepo()

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
            scheduleRepo.addPair(
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