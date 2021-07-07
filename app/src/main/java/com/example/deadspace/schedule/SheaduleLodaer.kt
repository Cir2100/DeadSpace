package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log

class SheaduleLodaer {

    private val localLoader = SheaduleLoaderLocalData()
    private val internetLoader = SheaduleLoaderInternet()
    //TODO: logic loader
    //load sheadule from local data or internet
    fun loadSheadule(name: String = "", isUsers : Boolean) {
        //TODO: checked local data
        if (isUsers && localLoader.checkedUsersSheadule(name)) {
            Log.e(ContentValues.TAG, "Load from local data")
            localLoader.loadShedule(name)
        }
        else {
            Log.e(ContentValues.TAG,"Load from local internet")
            internetLoader.loadShedule(name)
        }
    }
}