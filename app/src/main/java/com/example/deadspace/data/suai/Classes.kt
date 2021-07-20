package com.example.deadspace.data.suai

import kotlinx.serialization.Serializable

@Serializable
data class SemInfo(
    val Years: String,
    val IsAutumn: Boolean,
    val Update: String,
    val CurrentWeek: Int,
    val IsWeekOdd: Boolean,
    val IsWeekUp: Boolean,
    val IsWeekRed: Boolean
)

@Serializable
data class Group(
    val Name: String,
    val ItemId : Int
)

@Serializable
data class Teacher(
    val Name: String,
    val ItemId : Int
)

@Serializable
data class Build(
    val ItemOrd : Int,
    val Name: String,
    val Title: String,
    val ItemId : Int
)

@Serializable
data class Pair(
    val ItemId : Int,
    val Week : Int,
    val Day : Int,
    val Less : Int,
    val Build: String?,
    val Rooms: String?,
    val Disc: String,
    val Type: String,
    val Groups: String,
    val GroupsText: String,
    val Preps: String?,
    val PrepsText: String,
    val Dept: String?,

)

//TODO : in this area?
sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
