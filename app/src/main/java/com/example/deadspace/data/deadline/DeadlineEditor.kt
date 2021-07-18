package com.example.deadspace.data.deadline

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.MyDeadlinesDAO
import com.example.deadspace.data.database.MyDeadlinesData
import com.example.deadspace.data.database.getDatabase

class DeadlineEditor(private val myDeadlinesDAO: MyDeadlinesDAO) {

    private val res =  DeadSpace.appContext.resources

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?>
        get() = _error

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
            myDeadlinesDAO.insertOne(MyDeadlinesData(countDeadlines.value!!, title, discipline, lastDate, false))
            Log.i(this.javaClass.simpleName, "Add deadline successful")
            _error.value = null
            }
        else {
            Log.i(this.javaClass.simpleName, "This deadline in database yet")
            _error.value = res.getString(R.string.deadline_add_error)
        }

    }

    suspend fun deleteDeadline(id : Int) {
        myDeadlinesDAO.deleteOne(id)
        Log.i(this.javaClass.simpleName, "Delete deadline successful")
    }

    suspend fun changeDeadline(id : Int) {
        val deadline = myDeadlinesDAO.getOne(id)
        deadline.isDone = !deadline.isDone
        myDeadlinesDAO.updateOne(deadline)
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

