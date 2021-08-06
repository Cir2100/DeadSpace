package com.example.deadspace.data.schedule

import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*

class ScheduleSaver {

    private val myPairDAO = getPairDatabase(DeadSpace.appContext).myPairDAO
    private val myPairCashDAO = getPairCashDatabase(DeadSpace.appContext).myPairCashDAO
    private val myGroupAndTeacherDAO = getGroupAndTeacherDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    suspend fun saveCash(saveData : List<PairData>) {
        deleteCash()
        myPairCashDAO.insertAll(saveData)
    }

    suspend fun saveUserSchedule(saveData : List<MyPairData>) {
        myPairDAO.insertAll(saveData)
    }

    private suspend fun deleteCash() {
        myPairCashDAO.deleteCash()
    }

    suspend fun saveGroupList(items : List<GroupAndTeacherData>) {
        myGroupAndTeacherDAO.insertAll(items)
    }

}