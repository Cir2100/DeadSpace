package com.example.deadspace.ui.schedule

import android.text.Editable
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


    //user input
    //todo: class
    var nameGroupListener : String = "1942"

    var weekType : Boolean = true

    private val _weekText = MutableLiveData<String>("верхняя")
    val weekText : LiveData<String> = _weekText

    private var isUsers = false

    //TODO : this?
    private fun Boolean.toInt() = if (this) 1 else 0

    fun onChangeWeekType(){
        weekType = !weekType
        if (weekType)
            _weekText.postValue("верхняя")
        else
            _weekText.postValue("нижняя")
        onSearch()
    }

    //TODO : use anti
    fun onChangeIsUser() {
        isUsers = !isUsers
        onSearch()
    }

    //TODO: init user cash

    init {
        onSearch()
    }

    fun onSearch() {
        // TODO: users input
        val weekDay = 0

        viewModelScope.launch {
            try {
                scheduleLoader.loadSchedule(
                    name = nameGroupListener,
                    isUsers = isUsers,
                    weekType = weekType.toInt(),
                    weekDay
                )
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