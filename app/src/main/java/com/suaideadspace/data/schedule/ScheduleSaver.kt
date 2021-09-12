package com.suaideadspace.data.schedule

import com.suaideadspace.DeadSpace
import com.suaideadspace.data.database.*

class ScheduleSaver {

    private val database = getDatabase(DeadSpace.appContext)
    private val myPairDAO = database.myPairDAO
    private val myPairCashDAO = database.myPairCashDAO
    private val myGroupAndTeacherDAO = database.myGroupAndTeacherDAO

    suspend fun saveCash(saveData : List<PairData>) {
        deleteCash()
        saveData.forEach { it.isCash = true }
        myPairCashDAO.insertAll(saveData)
    }

    suspend fun saveUserSchedule(saveData : List<PairData>) {
        saveData.forEach { it.isCash = false }
        myPairDAO.insertAll(saveData)
    }

    private suspend fun deleteCash() {
        myPairCashDAO.deleteCash()
    }

    suspend fun saveGroupList(items : List<GroupAndTeacherData>) {
        myGroupAndTeacherDAO.insertAll(items)
    }

}

private lateinit var INSTANCE: ScheduleSaver

fun getScheduleSaver(): ScheduleSaver {
    synchronized(ScheduleSaver::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleSaver()
        }
    }
    return INSTANCE
}