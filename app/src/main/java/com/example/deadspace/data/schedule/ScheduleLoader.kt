package com.example.deadspace.data.schedule

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*

//@Singleton
class ScheduleLoader {

    private val myPairCashDAO = getDatabase(DeadSpace.appContext).myPairCashDAO

    private val localLoader = ScheduleLoaderLocalData()
    private val internetLoader = ScheduleLoaderInternet()

    //private var _weekType = 1
    //private var _weekDay = 0

    private val _pairs = MutableLiveData<List<PairData>>()
    var pairs: LiveData<List<PairData>> = _pairs

    private val _pairsCount = MutableLiveData<Int>()
    var pairsCount: LiveData<Int> = _pairsCount

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
        val daySchedule = myPairCashDAO.getDayCash(weekType , weekDay).sortedBy { it.Less }
        _pairs.value = daySchedule
        _pairsCount.value = daySchedule.size
    }

    //TODO
    suspend fun updateGroupAndTeacher() {
       /* if (getGroupAndTeacherDatabase(DeadSpace.appContext).myGroupAndTeacherDAO.getAll()
                .isEmpty()
        )*/
            internetLoader.loadGroupAndTeacher()
    }

    private suspend fun checkCash(name: String) : Boolean {

        val cash = myPairCashDAO.getCash()
        //TODO : add throw
        if (cash.size == 0)
            return false
        return cash[0].Name == name
    }

}