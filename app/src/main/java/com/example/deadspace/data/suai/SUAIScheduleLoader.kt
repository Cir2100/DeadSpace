package com.example.deadspace.data.suai

import android.util.Log


//TODO : rename

object SUAIScheduleLoader {

    suspend fun getGroups() : Result<List<Group>> {
        return if (::groups.isInitialized) {
            Result.Success(groups)
        } else {
            when(val result = loadGroupsList()) {
                is Result.Success<List<Group>> -> groups = result.data
                is Result.Error ->  {
                    Log.e(errorTag, "Groups don't load")
                    Log.e(errorTag, result.exception.message!!)
                    result
                }
            }
            Result.Success(groups)
        }
    }

    suspend fun getTeachers() : Result<List<Teacher>> {
        return if (::teachers.isInitialized) {
            Result.Success(teachers)
        } else {
            when(val result = loadTeachersList()) {
                is Result.Success<List<Teacher>> -> teachers = result.data
                is Result.Error -> {
                    Log.e(errorTag, "Teachers don't load")
                    Log.e(errorTag, result.exception.message!!)
                    result
                }
            }
            Result.Success(teachers)
        }
    }

    suspend fun getSemInfo() : Result<SemInfo> {
        return if (::semInfo.isInitialized) {
            Result.Success(semInfo)
        } else {
            when(val result = loadSemInfo()) {
                is Result.Success<SemInfo> -> semInfo = result.data
                is Result.Error -> {
                    Log.e(errorTag, "SemInfo don't load")
                    Log.e(errorTag, result.exception.message!!)
                    result
                }
            }
            Result.Success(semInfo)
        }
    }

    suspend fun getBuilds(): Result<List<Build>> = getObjectRes(getSemBuildsUrl)

    suspend fun getSchedule(name: String): Result<List<Pair>> {

        when (val result = getGroups()) {
            is Result.Success<List<Group>> -> {
                result.data.forEach {
                    if (it.Name == name)
                        return getObjectRes(getSemScheduleUrl + "/group" + it.ItemId)
                }
            }
            is Result.Error -> return result
        }

        when (val result = getTeachers()) {
            is Result.Success<List<Teacher>> -> {
                result.data.forEach {
                    if (it.Name == name)
                        return getObjectRes(getSemScheduleUrl + "/prep" + it.ItemId)
                }
            }
            is Result.Error -> return result
        }

        return Result.Error(Exception("Name $name does not exist"))
    }

    private val errorTag = this.javaClass.simpleName

    private lateinit var groups: List<Group>
    private lateinit var teachers: List<Teacher>
    private lateinit var semInfo: SemInfo

    private suspend fun loadGroupsList() : Result<List<Group>> = getObjectRes(getSemGroupUrl)

    private suspend fun loadTeachersList() : Result<List<Teacher>> = getObjectRes(getSemTeacherUrl)

    suspend fun loadSemInfo(): Result<SemInfo> = getObjectRes(getSemInfoUrl)

}