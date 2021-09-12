package com.suaideadspace.data.deadline

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.suaideadspace.DeadSpace
import com.suaideadspace.data.database.MyDeadlinesData
import com.suaideadspace.data.database.getDatabase

class DeadlineLoader {

    private val myDeadlinesDAO = getDatabase(DeadSpace.appContext).myDeadlinesDAO

    private var _deadlines : LiveData<List<MyDeadlinesData>> = myDeadlinesDAO.allDeadlines
    var deadlines : LiveData<List<MyDeadlinesData>> = Transformations.map(_deadlines) { list -> list.sortedBy { it.lastDate } }

    var countDeadlines : LiveData<Int> = myDeadlinesDAO.countDeadlines

    //TODO : load deadlines in one day
    //TODO : push-notification
}

private lateinit var INSTANCE: DeadlineLoader

fun getDeadlineLoader(): DeadlineLoader {
    synchronized(DeadlineLoader::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = DeadlineLoader()
        }
    }
    return INSTANCE
}