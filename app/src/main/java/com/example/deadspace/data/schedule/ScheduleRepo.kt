package com.example.deadspace.data.schedule

import androidx.lifecycle.MutableLiveData
import com.example.deadspace.data.database.PairData

class ScheduleRepo {

    private val scheduleLoader = getScheduleLoader()
    private val scheduleEditor = getScheduleEditor()

    val pairs = scheduleLoader.pairs
    val pairsCount = scheduleLoader.pairsCount

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?>
        get() = _error

    suspend fun loadSchedule(name: String, isUsers : Boolean) {
        _error.value = scheduleLoader.loadSchedule(name, isUsers)
    }

    suspend fun loadDay(weekType : Int, weekDay : Int) {
        scheduleLoader.loadDay(weekType, weekDay)
    }

    suspend fun updateGroupAndTeacher() {
        scheduleLoader.updateGroupAndTeacher()
    }

    suspend fun deletePair(pair : PairData) {
        scheduleEditor.deletePair(pair)
    }

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
        _error.value = scheduleEditor.addPair(name, weekDay, less, week, type,
            disc, teachers, groups, build, room)
    }
}

private lateinit var INSTANCE: ScheduleRepo

fun getScheduleRepo(): ScheduleRepo {
    synchronized(ScheduleRepo::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleRepo()
        }
    }
    return INSTANCE
}