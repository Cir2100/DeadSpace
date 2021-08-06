package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.database.MyPairData

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor(private val myPairDAO: MyPairDAO) {

    private val scheduleSaver = ScheduleSaver()

    private suspend fun loadSchedule(group : String) : MutableList<MyPairData> {
        Log.i(this.javaClass.simpleName, "Load user's schedule")
        var schedule = myPairDAO.getUserData(group)
        if (schedule.size == 0) {
            Log.i(this.javaClass.simpleName, "User schedule not in database")
            schedule = myPairDAO.getCash()
            for (pair in schedule) {
                pair.isCash = false
                pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 0
            }
            scheduleSaver.saveUserSchedule(schedule)
        }
        //if (schedule.size == 0)
            //TODO : throw ты дурачок
        return schedule
    }

    //TODO :use id

    suspend fun deletePair(time : String, week : Int, weekDay : Int, group : String) {
        var schedule = loadSchedule(group)

        myPairDAO.deleteUserPair(group, week, weekDay, time)

        schedule = myPairDAO.getUserData(group)
        for (pair in schedule) {
            pair.isCash = true
            pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 1
        }
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Delete pair successful")
    }

    //change local schedule
    suspend fun addPair(group : String, day : Int, time : String,
                        week : Int, type : String, name : String,
                        teachers : String, groups : String, address : String) {
        var schedule = loadSchedule(group)
        var isEntry = false
        for (pair in schedule)
        {
            if (pair.time == time && (pair.week == week || pair.week == 2) && pair.day == day)
                isEntry = true
        }
        if (!isEntry) {
            myPairDAO.insertOne(MyPairData(
                id = group.toInt() * 10000 + week * 1000 + day * 100 + time.toInt() * 10 + 0,
                group = group,
                day = day,
                time = time,
                week = week,
                type = type,
                name = name,
                teachers = teachers,
                groups = groups,
                address = address,
                isCash = false))

            schedule = myPairDAO.getUserData(group)
            for (pair in schedule) {
                pair.isCash = true
                pair.id = pair.group.toInt() * 10000 + pair.week * 1000 + pair.day * 100 + pair.time.toInt() * 10 + 1
            }
            scheduleSaver.saveCash(schedule)

            Log.i(this.javaClass.simpleName, "Add pair successful")
        }
        else {
            Log.i(this.javaClass.simpleName, "Pair in database yet")
            //TODO : throw
        }

    }
}