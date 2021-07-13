package com.example.deadspace.data.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

//@Singleton
class ScheduleLoader(private val myPairDao: MyPairDao) {

    private val localLoader = ScheduleLoaderLocalData(myPairDao)
    private val internetLoader = ScheduleLoaderInternet(myPairDao)

    private val _pair = MutableLiveData<List<MyPairData>>()
    var pairs: LiveData<List<MyPairData>> = _pair

    suspend fun loadSchedule(name: String, isUsers : Boolean, weekType : Int, weekDay : Int) {
        //TODO : checked cash normal
        if (!checkCash(name)) {
            if (isUsers) {
                Log.i(this.javaClass.simpleName, "Load from local data")
                localLoader.loadSchedule(name)
            }
            else {
                Log.i(this.javaClass.simpleName, "Load from internet")
                internetLoader.loadSchedule(name)
            }
        }
        loadPairs(weekType, weekDay)
    }

    private suspend fun loadPairs(weekType : Int, weekDay : Int) {
        _pair.postValue(myPairDao.getDayCash(weekType , weekDay))
    }

    private suspend fun checkCash(name: String) : Boolean {

        val cash = myPairDao.getCash()
        //TODO : add throw
        return cash[0].group == name
    }
}