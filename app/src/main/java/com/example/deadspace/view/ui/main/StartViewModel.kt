package com.example.deadspace.view.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.deadspace.schedule.MyPair
import com.example.deadspace.schedule.MyPairDao
import com.example.deadspace.schedule.MyPairData
import com.example.deadspace.schedule.ScheduleLoader
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


    /*private val _data = MutableLiveData<String>()
    val data : LiveData<String> = _data*/

    val data = scheduleLoader.pairs

    fun onSearch() {
        // TODO: users input
        val weekDay = 0
        val typeOfWeek = 1
        val name = "1942"
        val isUsers = true

        viewModelScope.launch {

            val i = try {
                //_data.value = ScheduleLoader().loadSchedule("1942", true)
                val result = scheduleLoader.loadSchedule(name = name, isUsers = isUsers)
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
                /*val result = mutableListOf(
                    MyPair("1 пара", 0, "Л", "name", "teachers", "groups", "adress"),
                    MyPair("1 пара", 1, "ПР", "name", "teachers", "groups", "adress")
                )
                Log.i(this.javaClass.simpleName, "Start load to database")
                for (pair in result) {
                   // if (pair.week == 2 || pair.week == typeOfWeek) {
                        Log.e(ContentValues.TAG, pair.time)
                        Log.e(ContentValues.TAG, pair.week.toString())
                        Log.e(ContentValues.TAG, pair.type)
                        Log.e(ContentValues.TAG, pair.name)
                        Log.e(ContentValues.TAG, pair.teachers)
                        Log.e(ContentValues.TAG, pair.groups)
                        Log.e(ContentValues.TAG, pair.address)
                //   }
                }*/
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

}