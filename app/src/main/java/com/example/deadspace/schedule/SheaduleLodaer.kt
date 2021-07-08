package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log

class SheaduleLodaer {

    private val localLoader = SheduleLoaderLocalData()
    private val internetLoader = SheduleLoaderInternet()
    //TODO: logic loader
    //load sheadule from local data or internet
    fun loadSheadule(name: String = "", isUsers : Boolean) {
        //TODO: checked local data
        if (isUsers && localLoader.checkedUsersShedule(name)) {
            Log.e(ContentValues.TAG, "Load from local data")
            localLoader.loadShedule(name)
        }
        else {
            Log.e(ContentValues.TAG,"Load from internet")
            internetLoader.loadShedule(name)
        }
    }
}