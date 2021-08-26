package com.example.deadspace.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["ItemId", "isCash"])
data class PairData(
    val ItemId : Int,
    val Name: String,
    val Week : Int,
    val Day : Int,
    val Less : Int,
    val StartTime : String,
    val EndTime : String,
    val Build: String,
    val Rooms: String,
    val Disc: String,
    val Type: String,
    val GroupsText: String,
    val TeachersText: String,
    var isCash: Boolean
)

@Entity
data class MyDeadlinesData constructor(
    @PrimaryKey val id: Int,
    val title : String,
    val discipline : String,
    val lastDate : String,
    var isDone : Boolean
    //TODO : add push-notification
)

@Entity
data class GroupAndTeacherData constructor(
    val ItemId : Int,
    @PrimaryKey val Name: String,
    val isGroup : Boolean
)