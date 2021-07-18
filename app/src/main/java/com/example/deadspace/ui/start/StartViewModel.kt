package com.example.deadspace.ui.start

import androidx.lifecycle.*
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.data.schedule.ScheduleLoader
import com.example.deadspace.ui.singleArgViewModelFactory

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel(private val myPairDAO: MyPairDAO) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::StartViewModel)
    }

}