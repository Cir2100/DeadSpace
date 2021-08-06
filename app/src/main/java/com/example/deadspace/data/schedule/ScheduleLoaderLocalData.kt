package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.data.database.MyPairDAO

class ScheduleLoaderLocalData(private val myPairDAO: MyPairDAO) {

    private val scheduleSaver = ScheduleSaver()

    //checked this group in local data
    fun checkedUsersSchedule(name: String) : Boolean {
        return false
    }

    //load schedule from local data
    suspend fun loadSchedule(name: String) {
        val schedule = myPairDAO.getUserData(name)
        for (pair in schedule) {
            pair.isCash = true
            pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 1
        }
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Load schedule from local data successful")
    }
}