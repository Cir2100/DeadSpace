package com.example.deadspace.schedule

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class SheduleLoaderInternet {

    enum class WeekDays {
        Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Воскресенье
    }

    private var groups : MutableList<Pair<Int, String>> = mutableListOf()
    private var teachers : MutableList<Pair<Int, String>> = mutableListOf()
    //TODO: use strings.xml
    private val shedule_link : String = "https://rasp.guap.ru/"

    //parse from rasp.guap.ru teachers and group
    private fun parseGroupAndTeacher() {
                val doc  = Jsoup.connect(shedule_link).get()
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
        Log.i(this.javaClass.simpleName, "Load list groups and teachers successful")
    }

    //find shedule group or teacher on rasp.guap.ru/?..
    //TODO : coroutine
    fun loadShedule(name : String) {
        Thread(Runnable {

            //checked name group for valid
            fun isCurrentGroup(name : String) : Int{
                for (group in groups) {
                    if (group.second == name)
                        return group.first
                }
                return -1
            }

            //checked name teacher for valid
            fun isCurrentTeacher(name : String) : Int{
                for (teacher in teachers) {
                    if (teacher.second == name)
                        return teacher.first
                }
                return -1
            }

            //load sheadule
            try {

                parseGroupAndTeacher()

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
                    throw Exception("Incorrect input")
                }
                var days : MutableList<MutableList<MyPair>> = mutableListOf(mutableListOf(), mutableListOf(),
                    mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

                val doc  = Jsoup.connect(shedule_link + letterPost + id.toString()).get()
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
                Log.i(this.javaClass.simpleName, "Load schedule successful")
                //return@Runnable days
            }
            catch (e: IOException) {
                Log.e(ContentValues.TAG, e.message.toString())
                // TODO: print error message "GUAP disconnect"
            }
            catch (e: Exception) {
                Log.e(ContentValues.TAG, e.message.toString())
                // TODO: print error message "Incorrect input"
            }

        }).start()
    }

    //parse shedule one day for pairs
    private fun parsePairs(daySheduleI: String) : MutableList<MyPair>{

        //parse group and teachers in pair
        fun parseGroupOrTeacher(input : String) : String {
            var tmp = ""
            var daySheduleTeachersOrGroups = input
            while (daySheduleTeachersOrGroups != ""){
                tmp += daySheduleTeachersOrGroups.substringAfter("\">").substringBefore("</a>") + ", "
                daySheduleTeachersOrGroups = daySheduleTeachersOrGroups.substringAfter("</a>")
            }
            tmp = tmp.substringBeforeLast(", ")
            return tmp
        }

        var dayShedule = daySheduleI.substringBeforeLast("</div>")
        var pairs : MutableList<MyPair> = mutableListOf()
        while (dayShedule.length > 10) {
            var time = dayShedule.substringAfter("<h4>").substringBefore("</h4>").trim()
            var pairInTime = dayShedule.substringAfter("</h4>").substringBefore("<h4>")
            dayShedule = dayShedule.substringAfter(pairInTime)
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