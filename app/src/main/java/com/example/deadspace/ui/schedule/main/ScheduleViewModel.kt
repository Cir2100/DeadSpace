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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class ScheduleViewModel : ViewModel() {

    private val scheduleRepo = getScheduleRepo()

    val myPairList = scheduleRepo.pairs
    val pairsCount = scheduleRepo.pairsCount

    var querySuggestions : List<GroupAndTeacherData> = listOf()

    private val _spinner = MutableLiveData(false)
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast : MutableLiveData<String?> = scheduleRepo.error
    val toast: LiveData<String?>
        get() = _toast

    private val _weekType = MutableLiveData(true)
    val weekType : LiveData<Boolean> = _weekType

    //TODO : interface and cash
    private val _colors = MutableLiveData(listOf(Color.RED, Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE,Color.WHITE))
    var colors : LiveData<List<Int>> = _colors

    private val _isUsers = MutableLiveData<Boolean>(true)
    val isUsers : LiveData<Boolean> = _isUsers

    var weekDay = 0

    var currentGroup : String = ""

    fun getWeekType() : Int {
        _weekType.value.let { return@let if (it == true) 1 else 0 }
        return 0
    }

    fun setIsUsers(isUser : Boolean) {
        _isUsers.value = isUser
    }

    fun onToastShown() {
        _toast.value = null
    }

    fun onChangeWeekType(){
        _weekType.value = !_weekType.value!!
        loadDaySchedule()
    }


    fun onChangeIsUser(isChange : Boolean) {
        _isUsers.value = isChange
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

    init {
        viewModelScope.launch {
            updateGroupAndTeacher()

            _weekType.value = scheduleRepo.loadWeekType()

            val date = Calendar.getInstance()
            val day = if (date.get(Calendar.DAY_OF_WEEK) - 2 >= 0) date.get(Calendar.DAY_OF_WEEK) - 2 else 6
            onClickWeekDay(day)

            loadDaySchedule()
        }
    }

    private fun loadDaySchedule() {
        viewModelScope.launch {
            scheduleRepo.loadDay(_weekType.value!!.toInt(), weekDay)
        }
    }

    fun loadDaySchedule(time : Long) {
        viewModelScope.launch {
            delay(500)
            scheduleRepo.loadDay(_weekType.value!!.toInt(), weekDay)
        }
    }

    fun onSearch(groupName : String) {

        viewModelScope.launch {
            try {
                _spinner.value = true
                currentGroup = groupName
                val errorMes = scheduleRepo.loadSchedule(
                    name = groupName,
                    isUsers = _isUsers.value!!
                )
                _toast.value = errorMes
                if (errorMes != null)
                    _isUsers.value = false
                loadDaySchedule()
            } catch (e: IOException) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                _toast.value = e.message.toString()
            } catch (e: Exception) {
                _spinner.value = false
                Log.e(this.javaClass.simpleName, e.message.toString())
                _toast.value = e.message.toString()
            } finally {
                _spinner.value = false
            }
        }
    }

    fun deleteUserSchedule() {
        viewModelScope.launch {
            scheduleRepo.deleteUserSchedule(currentGroup)
            _isUsers.value = false
            loadDaySchedule()
        }
    }

    fun onDeletePair(pair: PairData) {
        viewModelScope.launch {
            scheduleRepo.deletePair(pair)
            _isUsers.value = true
            loadDaySchedule()
        }
    }

    private fun updateGroupAndTeacher() {
        viewModelScope.launch {
            scheduleRepo.updateGroupAndTeacher()
            querySuggestions = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO.getAllSchedule()
        }
    }

    private fun Boolean.toInt() = if (this) 1 else 0

}

