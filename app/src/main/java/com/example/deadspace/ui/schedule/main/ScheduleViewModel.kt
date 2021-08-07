package com.example.deadspace.ui.schedule.main

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*
import com.example.deadspace.data.schedule.ScheduleEditor
import com.example.deadspace.data.schedule.ScheduleLoader
import com.example.deadspace.data.suai.SUAIScheduleLoader2
import com.example.deadspace.ui.singleArgViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class ScheduleViewModel(private val myPairDAO: MyPairDAO) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::ScheduleViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader()
    private val scheduleEditor = ScheduleEditor()

    val myPairList = scheduleLoader.pairs

    val pairsCount = scheduleLoader.pairsCount


    //TODO _----------------
    var _querySuggestions : List<GroupAndTeacherData> = listOf()



    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    //user input
    //todo: class

    private fun Boolean.toInt() = if (this) 1 else 0


    private var _weekTypeBool : Boolean = true //TODO : costil

    private val _weekType = MutableLiveData<Int>(1)
    val weekType : LiveData<Int> = _weekType

    private val _weekText = MutableLiveData<String>()
    val weekText : LiveData<String> = _weekText


    //TODO : interface and cash
    private val _colors = MutableLiveData<List<Int>>(listOf(Color.RED, Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE))
    var colors : LiveData<List<Int>> = _colors
//TODO : is false constructor
    var isUsers = false

    var weekDay = 0
//TODO : cash
    val currentGroupLive = MutableLiveData<String>() //TODO: delete this
    var currentGroup : String = ""

    fun onChangeWeekType(){
        _weekTypeBool = !_weekTypeBool
        _weekType.postValue(kotlin.math.abs(_weekType.value!! - 1))
        loadDaySchedule()
    }

    //TODO : use anti
    fun onChangeIsUser(isChange : Boolean) {
        isUsers = isChange
        //TODO : repair reload schedule
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

        viewModelScope.launch {
            _weekType.value = if (SUAIScheduleLoader2.getSemInfo().IsWeekUp) 1 else 0
            updateGroupAndTeacher()
        }


        val date = Calendar.getInstance()
        val day = if (date.get(Calendar.DAY_OF_WEEK) - 2 >= 0) date.get(Calendar.DAY_OF_WEEK) - 2 else 6
        onClickWeekDay(day) // TODO : refact this
        loadDaySchedule()
    }

    //TODO : load day after add and date

    fun loadDaySchedule() {
        viewModelScope.launch {
            scheduleLoader.loadDay(_weekTypeBool.toInt(), weekDay)
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
            } catch (e: IOException) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
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

    fun onDeletePair(pair: PairData) {
        viewModelScope.launch {
            scheduleEditor.deletePair(pair)
            isUsers = true //TODO it's don't working
            loadDaySchedule()
        }
    }

    fun updateGroupAndTeacher() {
        viewModelScope.launch {
            scheduleLoader.updateGroupAndTeacher()
            _querySuggestions = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO.getAll()
        }
    }


    }

