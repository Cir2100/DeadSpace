package com.example.deadspace.schedule

import androidx.room.Entity
import androidx.room.Fts4

/*class MyPair(_time : String, _week : Int, _type : String, _name : String, _teacher : String, _groups : String, _address : String) {
    val time: String = _time
    val week: Int = _week
    val type: String = _type
    val name: String = _name
    val teacher: String = _teacher
    val groups: String = _groups
    val address: String = _address
}*/

class MyPair(
    val time : String,
    val week : Int,
    val type : String,
    val name : String,
    val teacher : String,
    val groups : String,
    val address : String
)
