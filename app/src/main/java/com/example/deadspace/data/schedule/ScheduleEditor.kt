package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.*

//TODO: scheduleSaver @singleton and don't use this constructor
class ScheduleEditor {

    private val scheduleSaver = getScheduleSaver()
    private val pairTime = getSchedulePairTime()

    private val res =  DeadSpace.appContext.resources

    private val database = getDatabase(DeadSpace.appContext)
    private val myPairDAO = database.myPairDAO
    private val myPairCashDAO = database.myPairCashDAO
    private val myGroupAndTeacherDAO = database.myGroupAndTeacherDAO

    suspend fun deleteUserSchedule(name : String) {
        myPairDAO.deleteUserSchedule(name)
    }

    suspend fun deletePair(pair : PairData) {

        initUserSchedule(pair.Name)

        pair.isCash = false
        myPairDAO.deleteUserPair(pair)

        val schedule = myPairDAO.getUserSchedule(pair.Name)
        scheduleSaver.saveCash(schedule)
        Log.i(this.javaClass.simpleName, "Delete pair successful")
    }

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
                        room : String)  : String? {

        initUserSchedule(name)

        val pairTime = pairTime.getPairTime(less)

        val addId = myPairDAO.insertOne(PairData(
            ItemId = getItemId(name) * 10000 + week * 1000 + weekDay * 100 + less * 10,
            Name = name,
            Week = if (weekDay != 6) week else 2,
            Day = weekDay,
            Less = less,
            StartTime = pairTime.first,
            EndTime = pairTime.second,
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
            return res.getString(R.string.add_pair_error)
        }
        return null
    }

    private suspend fun initUserSchedule(name : String) {
        var schedule = myPairDAO.getUserSchedule(name)
        if (schedule.isEmpty()) {

            schedule = myPairCashDAO.getCash()
            scheduleSaver.saveUserSchedule(schedule)

            Log.i(this.javaClass.simpleName, "Init user's schedule")
        }
    }

    private suspend fun getItemId(name: String) : Int {
        myGroupAndTeacherDAO.getAllSchedule().forEach {
            if (it.Name == name)
                return it.ItemId
        }
        return -1
    }
}

private lateinit var INSTANCE: ScheduleEditor

fun getScheduleEditor(): ScheduleEditor {
    synchronized(ScheduleEditor::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleEditor()
        }
    }
    return INSTANCE
}