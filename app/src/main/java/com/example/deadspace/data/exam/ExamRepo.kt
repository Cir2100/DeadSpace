package com.example.deadspace.data.exam

import androidx.lifecycle.MutableLiveData

class ExamRepo {

    private val examLoaderLocalData = getExamLoaderLocalData()
    private val examLoaderInternet = getExamLoaderInternet()

    val listExams = examLoaderLocalData.listExams

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?>
        get() = _error

    suspend fun updateExams(name: String) {
        _error.value = examLoaderInternet.loadExams(name)
    }

    suspend fun updateGroupAndTeacher() {
        _error.value = examLoaderInternet.updateGroupAndTeacher()
    }

}

private lateinit var INSTANCE: ExamRepo

fun getExamLoaderExamRepo(): ExamRepo {
    synchronized(ExamRepo::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ExamRepo()
        }
    }
    return INSTANCE
}