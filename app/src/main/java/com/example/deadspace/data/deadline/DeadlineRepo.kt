package com.example.deadspace.data.deadline

class DeadlineRepo {

    private val deadlineLoader = getDeadlineLoader()
    private val deadlineEditor = getDeadlineEditor()

    val error = deadlineEditor.error
    val deadlines = deadlineLoader.deadlines
    val countDeadlines = deadlineLoader.countDeadlines

    suspend fun addDeadline(title : String, discipline : String, lastDate : String) {
        deadlineEditor.addDeadline(title, discipline, lastDate)
    }

    suspend fun deleteDeadline(id : Int) {
        deadlineEditor.deleteDeadline(id)
    }

    suspend fun changeDeadline(id : Int) {
        deadlineEditor.changeDeadline(id)
    }

}

private lateinit var INSTANCE: DeadlineRepo

fun getDeadlineRepo(): DeadlineRepo {
    synchronized(DeadlineRepo::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = DeadlineRepo()
        }
    }
    return INSTANCE
}

