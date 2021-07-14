package com.example.deadspace.data.schedule

import android.util.Log

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor(private val myPairDao: MyPairDao) {

    private val scheduleSaver = ScheduleSaver(myPairDao)

    private suspend fun loadSchedule(group : String) : MutableList<MyPairData> {
        Log.i(this.javaClass.simpleName, "Load user's schedule")
        var schedule = myPairDao.getUserData(group)
        if (schedule.size == 0) {
            Log.i(this.javaClass.simpleName, "User schedule not in database")
            schedule = myPairDao.getCash()
        }
        //if (schedule.size == 0)
            //TODO : throw ты дурачок
        return schedule
    }

    suspend fun deletePair(time : String, week : Int, weekDay : Int, group : String) {
        var schedule = myPairDao.getUserData(group)
        if (schedule.size == 0) {
            schedule = myPairDao.getCash()
            Log.i(this.javaClass.simpleName, "User schedule not in database")
            for (pair in schedule) {
                pair.isCash = false
                pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 0
            }
            scheduleSaver.saveUserSchedule(schedule)
        }
        myPairDao.deleteUserPair(group, week, weekDay, time)
        schedule = myPairDao.getUserData(group)
        for (pair in schedule) {
            pair.isCash = true
            pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 1
        }
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Delete pair successful")
    }

    //TODO: refactor?
    //change local schedule
    suspend fun addPair(group : String, day : Int, time : String,
                        week : Int, type : String, name : String,
                        teachers : String, groups : String, address : String) {
        val schedule = loadSchedule(group)
        var isEntry = false
        for (pair in schedule)
        {
            if (pair.time == time && (pair.week == week || pair.week == 2) && pair.day == day)
                isEntry = true

        }
        if (!isEntry) {
            Log.i(this.javaClass.simpleName, "Start add pair")
            schedule.add(MyPairData(
                id = group.toInt() * 10000 + week * 1000 + day * 100 + time.toInt() * 10 + 1,
                group = group,
                day = day,
                time = time,
                week = week,
                type = type,
                name = name,
                teachers = teachers,
                groups = groups,
                address = address,
                isCash = true
            ))

            Log.i(this.javaClass.simpleName, "Save cash")
            scheduleSaver.saveCash(schedule)
            Log.i(this.javaClass.simpleName, "Save cash successful")
            for (pair in schedule) {
                pair.isCash = false
                pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 0
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