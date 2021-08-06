package com.example.deadspace.data.suai

object SUAIScheduleLoader2 {

    suspend fun getGroups() : List<Group> {
        return if (::groups.isInitialized) {
            groups
        } else {
            groups = loadGroupsList()
            groups
        }
    }

    suspend fun getTeachers() : List<Teacher> {
        return if (::teachers.isInitialized) {
            teachers
        } else {
            teachers = loadTeachersList()
            teachers
        }
    }

    suspend fun getSemInfo() : SemInfo {
        return if (::semInfo.isInitialized) {
            semInfo
        } else {
            semInfo = loadSemInfo()
            semInfo
        }
    }

    suspend fun getBuilds(): List<Build> = getObject(getSemBuildsUrl)

    suspend fun getSchedule(name: String): List<Pair> {

        getGroups().forEach {
            if (it.Name == name)
                return getObject(getSemScheduleUrl + "/group" + it.ItemId)
        }

        getTeachers().forEach {
            if (it.Name == name)
                return getObject(getSemScheduleUrl + "/prep" + it.ItemId)
        }

        throw Exception("Name $name does not exist")
    }

    private val errorTag = this.javaClass.simpleName

    private lateinit var groups: List<Group>
    private lateinit var teachers: List<Teacher>
    private lateinit var semInfo: SemInfo

    private suspend fun loadGroupsList() : List<Group> = getObject(getSemGroupUrl)

    private suspend fun loadTeachersList() : List<Teacher> = getObject(getSemTeacherUrl)

    suspend fun loadSemInfo(): SemInfo = getObject(getSemInfoUrl)
}

