package com.example.deadspace.schedule

class ScheduleSaver(private val myPairDao: MyPairDao) {


    suspend fun saveCash(saveData : List<MyPairData>) {
        myPairDao.deleteCash()
        myPairDao.insertAll(saveData)
    }

    suspend fun saveUserSchedule(saveData : List<MyPairData>) {
        myPairDao.insertAll(saveData)
    }


}