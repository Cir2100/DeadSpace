package com.example.deadspace.schedule

import android.util.Log
import androidx.lifecycle.LiveData

class ScheduleLoaderLocalData(private val myPairDao: MyPairDao) {

    private val scheduleSaver = ScheduleSaver(myPairDao)

    //checked this group in local data
    fun checkedUsersSchedule(name: String) : Boolean {
        return false
    }

    //load schedule from local data
    suspend fun loadSchedule(name: String) {
        val schedule = myPairDao.getUserData(name)
        for (pair in schedule) {
            pair.isCash = true
        }
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Load schedule from local data successful")
    }
}