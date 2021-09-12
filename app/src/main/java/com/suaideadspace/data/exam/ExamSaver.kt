package com.suaideadspace.data.exam

import com.suaideadspace.DeadSpace
import com.suaideadspace.data.database.ExamData
import com.suaideadspace.data.database.GroupAndTeacherData
import com.suaideadspace.data.database.getDatabase

class ExamSaver {

    private val database = getDatabase(DeadSpace.appContext)
    private val myGroupAndTeacherDAO = database.myGroupAndTeacherDAO
    private val myExamDAO = database.myExamDAO

    suspend fun saveCash(saveData : List<ExamData>) {
        deleteCash()
        myExamDAO.insertAll(saveData)
    }

    private suspend fun deleteCash() {
        myExamDAO.deleteCash()
    }

    suspend fun saveGroupList(items : List<GroupAndTeacherData>) {
        myGroupAndTeacherDAO.insertAll(items)
    }

}

private lateinit var INSTANCE: ExamSaver

fun getExamSaver(): ExamSaver {
    synchronized(ExamSaver::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ExamSaver()
        }
    }
    return INSTANCE
}