package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class SheaduleLoaderInternet {

    enum class WeekDays {
        Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Воскресенье
    }

    private var groups : MutableList<Pair<Int, String>> = mutableListOf()
    private var teachers : MutableList<Pair<Int, String>> = mutableListOf()
    //TODO: use strings.xml
    private val sheadule_link : String = "https://rasp.guap.ru/"

    //parse from rasp.guap.ru teachers and group
    private fun parseGroupAndTeacher() {
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
    }

    //find sheadule group or teacher on rasp.guap.ru/?..
    fun loadShedule(name : String) :  MutableList<MutableList<MyPair>> {
        Thread(Runnable {

            parseGroupAndTeacher()

            //checed name group for valid
            fun isCurrentGroup(name : String) : Int{
                for (group in groups) {
                    if (group.second == name)
                        return group.first
                }
                return -1
            }

            //checed name teacher for valid
            fun isCurrentTeacher(name : String) : Int{
                for (teacher in teachers) {
                    if (teacher.second == name)
                        return teacher.first
                }
                return -1
            }
            try {
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
                    throw error("Incorrect input")
                    //Log.e(ContentValues.TAG, "Incorrect input")
                    // TODO: print error message
                }
                var days : MutableList<MutableList<MyPair>> = mutableListOf(mutableListOf(), mutableListOf(),
                    mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

                val doc  = Jsoup.connect(sheadule_link + letterPost + id.toString()).get()
                val result = doc.getElementsByAttributeValue( "class", "result")
                var html : String = result.toString().substringAfter("</h2>")
                while (html.contains("</h3>")) {
                    var weekday = html.substringAfter("<h3>").substringBefore("</h3>")
                    var pairs = html.substringAfter("</h3>").substringBefore("<h3>")
                    html = "<h3>" + html.substringAfter("</h3>").substringAfter("<h3>")
                    for (WeekDay in WeekDays.values()){
                        if(WeekDay.name == weekday)
                            days[WeekDay.ordinal] = parsePairs(pairs)
                    }
                }
                /*for (i in 0..6) {
                    Log.e(ContentValues.TAG, i.toString() + " " + days[i].size.toString())
                    for (pair in days[i])
                    {
                        Log.e(ContentValues.TAG, pair.time)
                        Log.e(ContentValues.TAG, pair.week)
                        Log.e(ContentValues.TAG, pair.type)
                        Log.e(ContentValues.TAG, pair.name)
                        Log.e(ContentValues.TAG, pair.teacher)
                        Log.e(ContentValues.TAG, pair.groups)
                        Log.e(ContentValues.TAG, pair.address)
                    }
                }*/
                return@Runnable days
            }
            catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message.toString())
            }
            catch (err) {
                Log.e(ContentValues.TAG, e.message.toString())
            }

        }).start()
    }

    //parse sheadule one day for pairs
    private fun parsePairs(daysheadulep: String) : MutableList<MyPair>{

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
            var time = daysheadule.substringAfter("<h4>").substringBefore("</h4>").trim()
            var pairInTime = daysheadule.substringAfter("</h4>").substringBefore("<h4>")
            daysheadule = daysheadule.substringAfter(pairInTime)
            while (pairInTime.length > 10) {
                var week = ""
                if (pairInTime.contains("class=\"up\"") || pairInTime.contains("class=\"dn\"")){
                    week = pairInTime.substringAfter("title=\"").substringBefore("\"").trim()
                    pairInTime = pairInTime.substringAfter("</b>")
                }
                else {
                    week = "bofs"
                }
                var type = pairInTime.substringAfter("<b>").substringBefore("</b>").trim()
                var name = pairInTime.substringAfter("–").substringBefore("<em>").trim()
                pairInTime = pairInTime.substringAfter("<em>")
                var adress = pairInTime.substringAfter("–").substringBefore("</em>").trim()
                var teachers = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                pairInTime = pairInTime.substringAfter("</a></span>")
                var groups  = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                pairInTime = pairInTime.substringAfter("</a></span>").substringAfter("</div>")
                pairs.add(MyPair(time, week, type, name, teachers, groups, adress))
            }
        }
        return pairs
    }
}