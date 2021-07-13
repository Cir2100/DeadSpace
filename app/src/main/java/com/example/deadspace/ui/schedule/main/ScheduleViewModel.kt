package com.example.deadspace.ui.schedule.main

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.data.schedule.MyPairDao
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.data.schedule.ScheduleLoader
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class ScheduleViewModel(private val myPairDao: MyPairDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::ScheduleViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader(myPairDao)

    val myPairList = scheduleLoader.pairs

    private val _isVisibleList = MutableLiveData<Boolean>()
    //todo no pair use this
    val isVisibleList : LiveData<Boolean> = _isVisibleList


    //user input
    //todo: class

    private var weekType : Boolean = true

    private val _weekText = MutableLiveData<String>("верхняя")
    val weekText : LiveData<String> = _weekText

    //TODO : interface and cash
    private val _colors = MutableLiveData<List<Int>>(listOf(Color.RED, Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE))
    var colors : LiveData<List<Int>> = _colors
//TODO : is false
    var isUsers = true

    private var weekDay = 0
//TODO : cash
    var currentGroup : String = "1942"

    //TODO : this?
    private fun Boolean.toInt() = if (this) 1 else 0

    fun onChangeWeekType(){
        weekType = !weekType
        if (weekType)
            _weekText.postValue("верхняя")
        else
            _weekText.postValue("нижняя")
        loadDaySchedule()
    }

    //TODO : use anti
    fun onChangeIsUser() {
        isUsers = !isUsers
        onSearch(currentGroup)
        loadDaySchedule()
    }

    fun onClickWeekDay(id : Int) {
        weekDay = id
        var colors : MutableList<Int> = mutableListOf()
        for (i in 0..6) {
            if (i == id)
                colors.add(i, Color.RED)
            else
                colors.add(i, Color.WHITE)

        }
        _colors.postValue(colors)
        loadDaySchedule()
    }

    //TODO: init user cash

    init {
        loadDaySchedule()
    }

    private fun loadDaySchedule() {
        viewModelScope.launch {
            scheduleLoader.loadDay(weekType.toInt(), weekDay)
        }
    }

    fun onSearch(groupName : String) {
        // TODO: users input
        viewModelScope.launch {
            try {
                currentGroup = groupName
                scheduleLoader.loadSchedule(
                    name = groupName,
                    isUsers = isUsers
                )
                loadDaySchedule()
                _isVisibleList.postValue(true)
            } catch (e: IOException) {
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
                _isVisibleList.postValue(false)
                Log.e(this.javaClass.simpleName, _isVisibleList.toString())
                //_data.value = e.message
            } catch (e: Exception) {
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
                //_data.value = e.message
            } finally {
                //TODO:
            }
            //myPairDao.insertAll(result)
            //TODO : use interface
        }
    }

}