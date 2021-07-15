package com.example.deadspace.data.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.database.MyPairData

//@Singleton
class ScheduleLoader(private val myPairDAO: MyPairDAO) {

    private val localLoader = ScheduleLoaderLocalData(myPairDAO)
    private val internetLoader = ScheduleLoaderInternet(myPairDAO)

    private val _pair = MutableLiveData<List<MyPairData>>()
    var pairs: LiveData<List<MyPairData>> = _pair

    suspend fun loadSchedule(name: String, isUsers : Boolean) {
        //TODO : checked cash normal
        if (isUsers) {
            Log.i(this.javaClass.simpleName, "Load from local data")
            localLoader.loadSchedule(name)
        }
        else {
            Log.i(this.javaClass.simpleName, "Load from internet")
            internetLoader.loadSchedule(name)
        }
    }

    suspend fun loadDay(weekType : Int, weekDay : Int) {
        Log.i(this.javaClass.simpleName, "Load from cash")
        _pair.postValue(myPairDAO.getDayCash(weekType , weekDay))
    }

    private suspend fun checkCash(name: String) : Boolean {

        val cash = myPairDAO.getCash()
        //TODO : add throw
        if (cash.size == 0)
            return false
        return cash[0].group == name
    }
}