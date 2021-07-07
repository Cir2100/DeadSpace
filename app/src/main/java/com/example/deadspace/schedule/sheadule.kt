package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log
import com.example.deadspace.R
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class sheadule {

    class MyPair(_time : String, _week : String, _type : String, _name : String, _teacher : String, _groups : String, _adress : String) {
        val time: String
        val week: String
        val type: String
        val name: String
        val teacher: String
        val groups: String
        val adress: String
        init{
            time = _time
            week = _week
            type = _type
            name = _name
            teacher = _teacher
            groups = _groups
            adress = _adress
        }
    }

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
                                // TODO: helps in input and dont split teachers
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
        var id = isCurrentGroup(name.trim())
        var letterPost : String = ""
        if (id != -1)
            letterPost = "?g="
        else {
            id = isCurrentTeacher(name.trim())
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
                var html : String = result.toString().substringAfter("</h2>")
                var days : MutableList<Pair<String, MutableList<MyPair>>> = mutableListOf()
                while (html.contains("</h3>")) {
                    var weekday = html.substringAfter("<h3>").substringBefore("</h3>")
                    var pairs = html.substringAfter("</h3>").substringBefore("<h3>")
                    html = "<h3>" + html.substringAfter("</h3>").substringAfter("<h3>")
                    Log.e(ContentValues.TAG,weekday+"::")
                    days.add(Pair(weekday, parsePairs(pairs)))
                }
                // TODO: work for days (days  contains name of week day and list of pairs in this day)
            } catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message.toString())
            }
        }
    }
    //parse sheadule one day for pairs
    fun parsePairs(daysheadulep: String) : MutableList<MyPair>{

        //parse group and teachers in pair
        fun parseGroupOrTeacher(input : String) : String {
            var tmp = ""
            var daySheaduleTeachersOrGroups = input
            while (daySheaduleTeachersOrGroups != ""){
                tmp += daySheaduleTeachersOrGroups.substringAfter("\">").substringBefore("</a>") + ", "
                daySheaduleTeachersOrGroups = daySheaduleTeachersOrGroups.substringAfter("</a>")
            }
            tmp = tmp.substringBeforeLast(", ")
            return tmp
        }

        var daysheadule = daysheadulep.substringBeforeLast("</div>")
        var pairs : MutableList<MyPair> = mutableListOf()
        while (daysheadule.length > 10) {
            var time = daysheadule.substringAfter("<h4>").substringBefore("</h4>")
            var pairInTime = daysheadule.substringAfter("</h4>").substringBefore("<h4>")
            daysheadule = daysheadule.substringAfter(pairInTime)
            while (pairInTime.length > 10) {
                var week = pairInTime.substringAfter("title=\"").substringBefore("\"")
                pairInTime = pairInTime.substringAfter("</b>").substringAfter("<b>")
                var type = pairInTime.substringBefore("</b>")
                var name = pairInTime.substringAfter("– ").substringBefore("<em>")
                pairInTime = pairInTime.substringAfter("<em>")
                var adress = pairInTime.substringAfter("– ").substringBefore("</em>")
                var teachers = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>"))
                pairInTime = pairInTime.substringAfter("</a></span>")
                var groups  = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>"))
                pairInTime = pairInTime.substringAfter("</a></span>").substringAfter("</div>")
                pairs.add(MyPair(time, week, type, name, teachers, groups, adress))
            }
        }
        return pairs
    }
}