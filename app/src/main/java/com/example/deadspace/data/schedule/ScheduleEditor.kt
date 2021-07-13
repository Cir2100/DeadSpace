package com.example.deadspace.data.schedule

import android.util.Log

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor(private val myPairDao: MyPairDao) {

    private val scheduleSaver = ScheduleSaver(myPairDao)

    /*private suspend fun loadSchedule(group : String) : MutableList<MyPairData> {
        Log.i(this.javaClass.simpleName, "Load user's schedule")
        var schedule = myPairDao.getUserData(group)
        if (schedule.size == 0) {
            Log.i(this.javaClass.simpleName, "User schedule not in database")
            ScheduleLoaderInternet(myPairDao).loadSchedule(group)
            schedule = myPairDao.getCash()
        }
        return schedule
    }*/

    //TODO: refactor?
    //change local schedule
    suspend fun addPair(group : String, day : Int, time : String,
                        week : Int, type : String, name : String,
                        teachers : String, groups : String, address : String) {
        //val schedule = loadSchedule(group)
        val schedule = myPairDao.getCash()
        var isEntry = false
        for (pair in schedule)
        {
            if (pair.group == group && pair.time == time && pair.week == week && pair.day == day)
                isEntry = true
        }
        if (!isEntry) {
            Log.i(this.javaClass.simpleName, "Add pair successful")
            /*schedule.add(
                MyPairData(name.toInt() * 10000 + week * 1000 + day * 100 + time.toInt() * 10 + 1,
                    group, day, time, week, type, name, teachers, groups,
                    address, true)
            )*/

            Log.i(this.javaClass.simpleName, "Save cash")
            scheduleSaver.saveCash(schedule)
            Log.i(this.javaClass.simpleName, "Save cash successful")
            for (pair in schedule) {
                pair.isCash = false
            }
            scheduleSaver.saveUserSchedule(schedule)
            Log.i(this.javaClass.simpleName, "Update database successful")
        }
        else {
            Log.i(this.javaClass.simpleName, "Pair in database yet")
            //TODO : throw
        }

    }
}