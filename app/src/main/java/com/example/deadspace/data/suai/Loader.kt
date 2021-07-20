package com.example.deadspace.data.suai

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//TODO : rename

object Loader {

    suspend fun getGroups() : List<Group> {
        return if (::groups.isInitialized) {
            groups
        } else {
            groups = when(val result = loadGroupsList()) {
                is Result.Success<List<Group>> -> result.data
                is Result.Error -> {
                    Log.e(errorTag, "Groups don't load")
                    Log.e(errorTag, result.exception.message!!)
                    listOf()
                }
            }
            groups
        }
    }

    suspend fun getTeachers() : List<Teacher> {
        return if (::teachers.isInitialized) {
            teachers
        } else {
            teachers = when(val result = loadTeachersList()) {
                is Result.Success<List<Teacher>> -> result.data
                is Result.Error -> {
                    Log.e(errorTag, "Teachers don't load")
                    Log.e(errorTag, result.exception.message!!)
                    listOf()
                }
            }
            teachers
        }
    }

    suspend fun getSemInfo(): Result<SemInfo> = getObject(getSemInfoUrl)

    suspend fun getBuilds(): Result<List<Build>> = getObject(getSemBuildsUrl)

    suspend fun getSchedule(name: String): Result<List<Pair>> {

        getGroups().forEach {
            if (it.Name == name)
                return getObject(getSemScheduleUrl + "/group" + it.ItemId)
        }

        getTeachers().forEach {
            if (it.Name == name)
                return getObject(getSemScheduleUrl + "/prep" + it.ItemId)
        }

        return Result.Error(Exception("Name $name does not exist"))
    }

    private val errorTag = this.javaClass.simpleName

    private lateinit var groups: List<Group>
    private lateinit var teachers: List<Teacher>

    private suspend fun loadGroupsList() : Result<List<Group>> = getObject(getSemGroupUrl)

    private suspend fun loadTeachersList() : Result<List<Teacher>> = getObject(getSemTeacherUrl)

}