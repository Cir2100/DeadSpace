package com.example.deadspace.schedule

import android.util.Log

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor(private val myPairDao: MyPairDao) {

    private val scheduleSaver = ScheduleSaver(myPairDao)

    //change local schedule
    suspend fun addPair(group : String, day : Int, time : String,
                        week : Int, type : String, name : String,
                        teachers : String, groups : String, address : String) {
        val schedule = myPairDao.getCash()
        schedule.add(MyPairData(group, day, time, week, type, name, teachers, groups,
            address, false))
        scheduleSaver.saveCash(schedule)
        for (pair in schedule) {
            pair.isCash = false
        }
        scheduleSaver.saveUserSchedule(schedule)
        Log.i(this.javaClass.simpleName, "Add pair successful")
    }
}