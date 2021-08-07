package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor {

    private val scheduleSaver = ScheduleSaver()
    private val myPairDAO = getDatabase(DeadSpace.appContext).myPairDAO
    private val myPairCashDAO = getDatabase(DeadSpace.appContext).myPairCashDAO
    private val myGroupAndTeacherDAO = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    private suspend fun loadSchedule(name : String) : List<PairData> {
        Log.i(this.javaClass.simpleName, "Load user's schedule")
        var schedule = myPairDAO.getUserSchedule(name)
        if (schedule.isEmpty()) {
            Log.i(this.javaClass.simpleName, "User schedule not in database")
            schedule = myPairCashDAO.getCash() as List<PairData>
            scheduleSaver.saveUserSchedule(schedule)
        }
        //if (schedule.size == 0)
            //TODO : throw ты дурачок
        return schedule
    }

    private suspend fun initUserSchedule(name : String) {
        var schedule = myPairDAO.getUserSchedule(name)
        if (schedule.isEmpty()) {

            schedule = myPairCashDAO.getCash()
            scheduleSaver.saveUserSchedule(schedule)

            Log.i(this.javaClass.simpleName, "Init user's schedule")
        }
    }

    suspend fun deletePair(pair : PairData) {

        initUserSchedule(pair.Name)

        pair.isCash = false
        myPairDAO.deleteUserPair(pair)

        val schedule = myPairDAO.getUserSchedule(pair.Name)
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Delete pair successful")
    }

    //TODO if day = 6, then week = 2
    //change local schedule
    suspend fun addPair(name : String,
                        weekDay : Int,
                        less : Int,
                        week : Int,
                        type : String,
                        disc : String,
                        teachers : String,
                        groups : String,
                        build : String,
                        room : String) {

        initUserSchedule(name)

        val addId = myPairDAO.insertOne(PairData(
            ItemId = getItemId(name) * 10000 + week * 1000 + weekDay * 100 + less * 10,
            Name = name,
            Week = week,
            Day = weekDay,
            Less = less,
            Build = build,
            Rooms = room,
            Disc = disc,
            Type = type,
            GroupsText = groups,
            TeachersText = teachers,
            isCash = false)
        )

        val schedule = myPairDAO.getUserSchedule(name)
        scheduleSaver.saveCash(schedule)


        if (addId != (-1).toLong()) {

            Log.i(this.javaClass.simpleName, "Add pair successful")
        } else {
            Log.i(this.javaClass.simpleName, "Pair in database yet")
            //TODO : throw
        }
    }

    private suspend fun getItemId(name: String) : Int {
        myGroupAndTeacherDAO.getAll().forEach {
            if (it.Name == name)
                return it.ItemId
        }
        return -1
    }
}