package com.example.deadspace.view.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.schedule.*
import kotlinx.coroutines.launch
import java.io.IOException

//TODO: use Hilt
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

    var nameGroup : String = ""


    fun onSearch() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val name = nameGroup
        val isUsers = true

        viewModelScope.launch {
            try {
                scheduleLoader.loadSchedule(name = name, isUsers = isUsers)
                /*for (pair in result[weekDay]) {
                    if (pair.week == 2 || pair.week == typeOfWeek) {
                        myPairDao.insertAll(MyPairData(pair.time, pair.week,
                            pair.type, pair.name, pair.teachers, pair.groups, pair.address, true))
                        Log.e(ContentValues.TAG, pair.time)
                        Log.e(ContentValues.TAG, pair.week.toString())
                        Log.e(ContentValues.TAG, pair.type)
                        Log.e(ContentValues.TAG, pair.name)
                        Log.e(ContentValues.TAG, pair.teachers)
                        Log.e(ContentValues.TAG, pair.groups)
                        Log.e(ContentValues.TAG, pair.address)
                    }
            }*/
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
            /*for (pair in result[weekDay]) {
                if (pair.week == 2 || pair.week == typeOfWeek) {
                    Log.e(ContentValues.TAG, pair.time)
                    Log.e(ContentValues.TAG, pair.week.toString())
                    Log.e(ContentValues.TAG, pair.type)
                    Log.e(ContentValues.TAG, pair.name)
                    Log.e(ContentValues.TAG, pair.teacher)
                    Log.e(ContentValues.TAG, pair.groups)
                    Log.e(ContentValues.TAG, pair.address)
                }
            }*/
        }
    }

    fun addPair() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val name = "1932"
        val namePair = "Технология программирования"
        val type = "Л"
        val time = "11:30"
        val teachers = "Преподы"
        val groups = "Группы"
        val address = "Адресс"

        viewModelScope.launch {
            scheduleEditor.addPair(name, weekDay, time, typeOfWeek, type, namePair, teachers, groups,
                address)
        }
    }

}