package com.example.deadspace.data.deadline

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.database.MyDeadlinesData
import java.time.LocalDate

class DeadlineEditor(private val myDeadlinesDAO: MyDeadlinesDAO) {

    var deadlines : LiveData<List<MyDeadlinesData>> = myDeadlinesDAO.allDeadlines //TODO: in loader

    suspend fun addDeadline(title : String, discipline : String, lastDate : String){
        val deadlines = myDeadlinesDAO.getAllDeadlines()
        var isEntry : Boolean = false
        for (deadline in deadlines) {
            if (deadline.title == title && deadline.discipline == discipline && deadline.lastDate == lastDate)
                isEntry = true
        }
        if (!isEntry) {
            myDeadlinesDAO.insertOne(MyDeadlinesData(title, discipline, lastDate))
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
}