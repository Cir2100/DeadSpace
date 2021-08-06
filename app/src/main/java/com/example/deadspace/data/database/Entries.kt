package com.example.deadspace.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Entity
data class MyPairData constructor(
    @PrimaryKey @ColumnInfo(name = "rowid") var id: Int,
    val group: String,
    val day : Int,
    val time : String,
    val week : Int,
    val type : String,
    val name : String,
    val teachers : String,
    val groups : String,
    val address : String,
    var isCash : Boolean //TODO: ??? val
)

@Entity
data class PairData(
    @PrimaryKey val ItemId : Int,
    val Week : Int,
    val Day : Int,
    val Less : Int,
    val Build: String,
    val Rooms: String,
    val Disc: String,
    val Type: String,
    val GroupsText: String,
    val TeachersText: String
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
    val isGroup : Boolean,
    val isUserHasOwn : Boolean
)