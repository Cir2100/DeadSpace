package com.example.deadspace.data.schedule

import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*

class ScheduleSaver {

    private val myPairDAO = getDatabase(DeadSpace.appContext).myPairDAO
    private val myPairCashDAO = getDatabase(DeadSpace.appContext).myPairCashDAO
    private val myGroupAndTeacherDAO = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

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