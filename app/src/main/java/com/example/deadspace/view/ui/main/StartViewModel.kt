package com.example.deadspace.view.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.schedule.*
import kotlinx.coroutines.launch
import java.io.IOException

//TODO: use Hilt
//TODO : use FLOW?
//@HiltViewModel
class StartViewModel(private val myPairDao: MyPairDao) : ViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::StartViewModel)
    }

    //TODO: this in constructor
    private val scheduleLoader = ScheduleLoader(myPairDao)
    private val scheduleEditor = ScheduleEditor(myPairDao)


    /*private val _data = MutableLiveData<String>()
    val data : LiveData<String> = _data*/

    //val data = scheduleLoader.pairs

    val myPairList = scheduleLoader.pairs

    var nameGroupListener : String = ""
    var isUsersListener = MutableLiveData<Boolean>(false)

    //TODO : use anti
    fun onChangeIsUser(isCecked : Boolean) {
        //Log.e(this.javaClass.simpleName, isUsersListener.value!!.toString())
        //onSearch()
    }

    //TODO: if empty users check
    fun onSearch() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1

        viewModelScope.launch {
            try {
                scheduleLoader.loadSchedule(name = nameGroupListener, isUsers = isUsersListener.value!!)
        } catch (e: IOException) {
                Log.e(this.javaClass.simpleName, e.message.toString())
                //TODO: print err message in activity
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

    fun addPair() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val namePair = "Технология программирования"
        val type = "Л"
        val time = "11:30"
        val teachers = "Преподы"
        val groups = "Группы"
        val address = "Адресс"

        viewModelScope.launch {
            scheduleEditor.addPair(nameGroupListener, weekDay, time, typeOfWeek, type, namePair, teachers, groups,
                address)
        }
    }

}