package com.example.deadspace.data.schedule

import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*

class ScheduleSaver() {

    private val myPairDAO = getPairDatabase(DeadSpace.appContext).myPairDAO
    private val myGroupAndTeacherDAO = getGroupAndTeacherDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    suspend fun saveCash(saveData : List<MyPairData>) {
        deleteCash()
        myPairDAO.insertAll(saveData)
    }

    suspend fun saveUserSchedule(saveData : List<MyPairData>) {
        myPairDAO.insertAll(saveData)
    }

    suspend fun deleteCash() {
        myPairDAO.deleteCash()
    }

    suspend fun saveGroupList(items : List<GroupAndTeacherData>) {
        myGroupAndTeacherDAO.insertAll(items)
    }

}