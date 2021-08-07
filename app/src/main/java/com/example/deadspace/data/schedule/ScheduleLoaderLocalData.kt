package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.getDatabase

class ScheduleLoaderLocalData {

    private val res =  DeadSpace.appContext.resources

    private val scheduleSaver = getScheduleSaver()
    private val myPairDAO = getDatabase(DeadSpace.appContext).myPairDAO


    //load schedule from local data
    suspend fun loadSchedule(name: String) : String? {
        val schedule = myPairDAO.getUserSchedule(name)
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Load schedule from local data successful")

        if (schedule.isEmpty()) {
            return res.getString(R.string.load_user_schedule_error)
        }
        return null
    }
}

private lateinit var INSTANCE: ScheduleLoaderLocalData

fun getScheduleLoaderLocalData(): ScheduleLoaderLocalData {
    synchronized(ScheduleLoaderLocalData::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleLoaderLocalData()
        }
    }
    return INSTANCE
}