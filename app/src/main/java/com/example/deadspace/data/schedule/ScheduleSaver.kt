package com.example.deadspace.data.schedule

import com.example.deadspace.data.database.MyPairDAO
import com.example.deadspace.data.database.MyPairData

class ScheduleSaver(private val myPairDAO: MyPairDAO) {


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


}