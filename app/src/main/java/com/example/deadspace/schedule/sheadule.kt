package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log
import com.example.deadspace.R
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class sheadule {
    var groups : MutableList<Pair<Int, String>> = mutableListOf()
    var teachers : MutableList<Pair<Int, String>> = mutableListOf()
    val sheadule_link : String = "https://rasp.guap.ru/"
    //parse from rasp.guap.ru teachers and group
    fun parseGroupAndTeacher() {
        Thread(Runnable {
            try {
                val doc  = Jsoup.connect(sheadule_link).get()
                val selecteds: Elements = doc.select("select")
                for (i in 0..1){
                    var options = selecteds[i].select("option")
                    for (option in options) {
                        if (option.attr("value").toInt() != -1){
                            if (i == 0)
                                groups.add(Pair(option.attr("value").toInt(), option.text()))
                            else if(i == 1)
                                teachers.add(Pair(option.attr("value").toInt(), option.text().substringBefore(" -")))
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message.toString())
            }

            parseShedule("1942")

        }).start()
    }
    //checed name group for valid
    private fun isCurrentGroup(name : String) : Int{
        for (group in groups) {
            if (group.second == name)
                return group.first
        }
        return -1
    }
    //checed name teacher for valid
    private fun isCurrentTeacher(name : String) : Int{
        for (teacher in teachers) {
            if (teacher.second == name)
                return teacher.first
        }
        return -1
    }
    //find sheadule group or teacher on rasp.guap.ru/?..
    fun parseShedule(name : String) {
        var id = isCurrentGroup(name)
        var letterPost : String = ""
        if (id != -1)
            letterPost = "?g="
        else {
            id = isCurrentTeacher(name)
            if (id != -1)
                letterPost = "?p="
        }
        if (id == -1){
            Log.e(ContentValues.TAG, "Incorrect input")
            // TODO: print error message
        }
        else{
            try {
                val doc  = Jsoup.connect(sheadule_link + letterPost + id.toString()).get()
                val result = doc.getElementsByAttributeValue( "class", "result")
                val result2 = result.next()
                val h3s = doc.select( "h3")
                // TODO: parse sheadule
                Log.e(ContentValues.TAG, "AAAAA")
                Log.e(ContentValues.TAG, result.toString())
                Log.e(ContentValues.TAG, "AAAAA")
                Log.e(ContentValues.TAG, result2.toString())
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message.toString())
            }
        }
    }
}