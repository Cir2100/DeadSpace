package com.example.deadspace.data.exam

import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.getDatabase

class ExamLoaderLocalData {

    private val myExamDAO = getDatabase(DeadSpace.appContext).myExamDAO

    val listExams = myExamDAO.allExams

}

private lateinit var INSTANCE: ExamLoaderLocalData

fun getExamLoaderLocalData(): ExamLoaderLocalData {
    synchronized(ExamLoaderLocalData::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ExamLoaderLocalData()
        }
    }
    return INSTANCE
}