package com.example.deadspace.data.deadline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.database.MyDeadlinesData
import com.example.deadspace.data.database.getDatabase

class DeadlineEditor(private val myDeadlinesDAO: MyDeadlinesDAO) {


    private var _deadlines : LiveData<List<MyDeadlinesData>> = myDeadlinesDAO.allDeadlines //TODO: in loader
    var deadlines : LiveData<List<MyDeadlinesData>> = Transformations.map(_deadlines) { list -> list.sortedBy { it.lastDate } }

    var countDeadlines : LiveData<Int> = myDeadlinesDAO.countDeadlines


    suspend fun addDeadline(title : String, discipline : String, lastDate : String){
        val deadlines = myDeadlinesDAO.getAllDeadlines()
        var isEntry = false
        for (deadline in deadlines) {
            if (deadline.title == title && deadline.discipline == discipline && deadline.lastDate == lastDate)
                isEntry = true
        }
        if (!isEntry) {
            myDeadlinesDAO.insertOne(MyDeadlinesData(title, discipline, lastDate, false))
            Log.i(this.javaClass.simpleName, "Add deadline successful")
        }
        else {
            //TODO : throw
            Log.i(this.javaClass.simpleName, "This deadline in database yet")
        }

    }

    suspend fun deleteDeadline(title : String, discipline : String, lastDate : String) {
        myDeadlinesDAO.deleteOne(title, discipline, lastDate)
        Log.i(this.javaClass.simpleName, "Delete deadline successful")
    }
//TODO : use update id = countDeadlines
    suspend fun changeDeadline(title : String, discipline : String, lastDate : String, isDone : Boolean) {
        myDeadlinesDAO.deleteOne(title, discipline, lastDate)
        myDeadlinesDAO.insertOne(MyDeadlinesData(title, discipline, lastDate, isDone))
        Log.i(this.javaClass.simpleName, "Change deadline successful")
    }
}

private lateinit var INSTANCE: DeadlineEditor

fun getDeadlineEditor(): DeadlineEditor {
    synchronized(DeadlineEditor::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = DeadlineEditor(getDatabase(DeadSpace.appContext).myDeadlinesDAO)
        }
    }
    return INSTANCE
}

