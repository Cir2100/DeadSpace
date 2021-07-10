package com.example.deadspace.schedule

import android.util.Log
import androidx.lifecycle.LiveData

//@Singleton
class ScheduleLoader(private val myPairDao: MyPairDao) {

    private val localLoader = ScheduleLoaderLocalData(myPairDao)
    private val internetLoader = ScheduleLoaderInternet(myPairDao)

    val pairs: LiveData<MyPairData> = myPairDao.cashLiveData

    //TODO: logic loader
    //load scheadule from local data or internet
    /*fun loadSchedule(name: String = "", isUsers : Boolean) {
        //TODO: checked local data
        if (isUsers && localLoader.checkedUsersShedule(name)) {
            Log.e(ContentValues.TAG, "Load from local data")
            localLoader.loadShedule(name)
        }
        else {
            Log.e(ContentValues.TAG,"Load from internet")
            internetLoader.loadShedule(name)
        }
    }*/

    suspend fun loadSchedule(name: String, isUsers : Boolean) {
        //TODO: checked local data
        if (isUsers && localLoader.checkedUsersSchedule(name)) {
            Log.i(this.javaClass.simpleName, "Load from local data")
            localLoader.loadSchedule(name)
        }
        else {
            Log.i(this.javaClass.simpleName, "Load from internet")
            internetLoader.loadSchedule(name)
        }
    }
}