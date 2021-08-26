package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.PairData
import com.example.deadspace.data.database.getDatabase

class ScheduleLoaderLocalData {

    private val res =  DeadSpace.appContext.resources

    private val scheduleSaver = getScheduleSaver()
    private val pairTime = getSchedulePairTime()

    private val database = getDatabase(DeadSpace.appContext)
    private val myPairDAO = database.myPairDAO
    private val myPairCashDAO = database.myPairCashDAO


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

    suspend fun loadCurrentPair(time : Int, weekType: Int , weekDay: Int) : PairData? {
        val cash = myPairCashDAO.getCash()
        Log.e("fdgfg", weekType.toString())
        Log.e("fdgfg", weekDay.toString())
        cash.forEach {
            if ((pairTime.toTime(it.StartTime) <= time && time <= pairTime.toTime(it.EndTime))
                        && (it.Week == 2 || it.Week == weekType)
                        && it.Day == weekDay) {
                return it
            }
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