package com.example.deadspace.ui.schedule.main

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*
import com.example.deadspace.data.schedule.getScheduleRepo
import com.example.deadspace.data.suai.SUAIScheduleLoader2
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class ScheduleViewModel : ViewModel() {

    //TODO: this in constructor
    private val scheduleRepo = getScheduleRepo()

    val myPairList = scheduleRepo.pairs

    val pairsCount = scheduleRepo.pairsCount


    //TODO _----------------
    var _querySuggestions : List<GroupAndTeacherData> = listOf()



    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _snackBar : MutableLiveData<String?> = scheduleRepo.error
    val snackBar: LiveData<String?>
        get() = _snackBar

    //user input
    //todo: class

    private fun Boolean.toInt() = if (this) 1 else 0


    private var _weekTypeBool : Boolean = true //TODO : costil

    private val _weekType = MutableLiveData<Int>(1)
    val weekType : LiveData<Int> = _weekType

    private val _weekText = MutableLiveData<String>()
    val weekText : LiveData<String> = _weekText


    //TODO : interface and cash
    private val _colors = MutableLiveData(listOf(Color.RED, Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE))
    var colors : LiveData<List<Int>> = _colors
//TODO : is false constructor
    var isUsers = false

    var weekDay = 0
//TODO : cash
    val currentGroupLive = MutableLiveData<String>() //TODO: delete this
    var currentGroup : String = ""

    fun onSnackBarShown() {
        _snackBar.value = null
    }

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
            scheduleRepo.loadDay(_weekTypeBool.toInt(), weekDay)
        }
    }

    fun onSearch(groupName : String) {
        // TODO: users input
        viewModelScope.launch {
            try {
                _spinner.value = true
                currentGroup = groupName
                currentGroupLive.postValue(currentGroup)
                scheduleRepo.loadSchedule(
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
            scheduleRepo.deletePair(pair)
            isUsers = true //TODO it's don't working
            loadDaySchedule()
        }
    }

    fun updateGroupAndTeacher() {
        viewModelScope.launch {
            scheduleRepo.updateGroupAndTeacher()
            _querySuggestions = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO.getAll()
        }
    }


    }

