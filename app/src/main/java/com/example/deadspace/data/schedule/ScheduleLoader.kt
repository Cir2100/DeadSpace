package com.example.deadspace.data.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.database.MyPairData

//@Singleton
class ScheduleLoader(private val myPairDAO: MyPairDAO) {

    private val localLoader = ScheduleLoaderLocalData(myPairDAO)
    private val internetLoader = ScheduleLoaderInternet(myPairDAO)

    //private var _weekType = 1
    //private var _weekDay = 0

    private val _pair = MutableLiveData<List<MyPairData>>()
    var pairs: LiveData<List<MyPairData>> = _pair
    //var pairs: LiveData<List<MyPairData>> = Transformations.map(myPairDAO.allCash, ::loadDay2)

    /*private fun loadDay2(schedule :  List<MyPairData>) : List<MyPairData> {
        //Log.i(this.javaClass.simpleName, "Load from cash")
        val daySchedule : MutableList<MyPairData> = mutableListOf()
        Log.e("sdf", _weekDay.toString())
        Log.e("sdf", _weekType.toString())
        for (pair in schedule) {
            if (pair.day == _weekDay && (pair.week == _weekType || pair.week == 2))
                daySchedule.add(pair)
        }
        daySchedule.sortBy { it.time }
        return daySchedule
    }*/

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
        //_weekDay = weekDay
        //_weekType = weekType
        Log.i(this.javaClass.simpleName, "Load from cash")
        _pair.postValue(myPairDAO.getDayCash(weekType , weekDay).sortedBy { it.time })
    }

    private suspend fun checkCash(name: String) : Boolean {

        val cash = myPairDAO.getCash()
        //TODO : add throw
        if (cash.size == 0)
            return false
        return cash[0].group == name
    }
}