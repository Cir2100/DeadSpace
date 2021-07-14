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
    private val scheduleEditor = ScheduleEditor(myPairDao)

    val myPairList = scheduleLoader.pairs

    private val _isVisibleList = MutableLiveData<Boolean>()
    //todo no pair use this
    val isVisibleList : LiveData<Boolean> = _isVisibleList



    private val _spinner = MutableLiveData<Boolean>(false)

    /**
     * Show a loading spinner if true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    //user input
    //todo: class

    private var weekType : Boolean = true

    private val _weekText = MutableLiveData<String>("верхняя")
    val weekText : LiveData<String> = _weekText

    //TODO : interface and cash
    private val _colors = MutableLiveData<List<Int>>(listOf(Color.RED, Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE))
    var colors : LiveData<List<Int>> = _colors
//TODO : is false constructor
    var isUsers = false

    private var weekDay = 0
//TODO : cash
    val currentGroupLive = MutableLiveData<String>() //TODO: delete this
    var currentGroup : String = ""

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
    fun onChangeIsUser(isChange : Boolean) {
        isUsers = !isChange
        Log.e(this.javaClass.simpleName, "THIS") //TODO : repair reload schedule
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
            try{
                _spinner.value = true
                scheduleLoader.loadDay(weekType.toInt(), weekDay)
            } finally {
                _spinner.value = false
            }

        }
    }

    fun onSearch(groupName : String) {
        // TODO: users input
        viewModelScope.launch {
            try {
                _spinner.value = true
                currentGroup = groupName
                currentGroupLive.postValue(currentGroup)
                scheduleLoader.loadSchedule(
                    name = groupName,
                    isUsers = isUsers
                )
                loadDaySchedule()
                _isVisibleList.postValue(true)
            } catch (e: IOException) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
                _isVisibleList.postValue(false)
                Log.e(this.javaClass.simpleName, _isVisibleList.toString())
                //_data.value = e.message
            } catch (e: Exception) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
                //_data.value = e.message
            } finally {
                _spinner.value = false
                //TODO:
            }
            //myPairDao.insertAll(result)
            //TODO : use interface
        }
    }

    fun onDeletePair(time : String) {
        viewModelScope.launch {
            scheduleEditor.deletePair(time, weekType.toInt(), weekDay, currentGroup)
            loadDaySchedule()
            isUsers = false //TODO: change switch
        }
    }

}