package com.example.deadspace.data.schedule

import android.content.ContentValues
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class ScheduleLoaderInternet(private val myPairDao: MyPairDao) {

    private val scheduleSaver = ScheduleSaver(myPairDao)

    enum class WeekDays {
        Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Вне
    }

    private var groups : MutableList<Pair<Int, String>> = mutableListOf()
    private var teachers : MutableList<Pair<Int, String>> = mutableListOf()
    //TODO: use strings.xml
    private val schedule_link : String = "https://rasp.guap.ru/"

    //parse from rasp.guap.ru teachers and group
    suspend private fun parseGroupAndTeacher() {
                val doc  = Jsoup.connect(schedule_link).get()
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

    //TODO : normal parse and refactor
    //find schedule group or teacher on rasp.guap.ru/?..
    suspend fun loadSchedule(name : String) {

        var days: MutableList<MutableList<MyPair>> = mutableListOf(
            mutableListOf(), mutableListOf(),
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

        var daysData: MutableList<MyPairData> = mutableListOf()

        withContext(Dispatchers.IO) {

            //checked name group for valid
            suspend fun isCurrentGroup(name: String): Int {
                for (group in groups) {
                    if (group.second == name)
                        return group.first
                }
                return -1
            }

            //checked name teacher for valid
            suspend fun isCurrentTeacher(name: String): Int {
                for (teacher in teachers) {
                    if (teacher.second == name)
                        return teacher.first
                }
                return -1
            }

            //load schedule
            try {

                parseGroupAndTeacher()

                var id = isCurrentGroup(name.trim())
                var letterPost: String = ""
                if (id != -1)
                    letterPost = "?g="
                else {
                    id = isCurrentTeacher(name.trim())
                    if (id != -1)
                        letterPost = "?p="
                }
                if (id == -1) {
                    throw Exception("Incorrect input")
                }

                val doc = Jsoup.connect(schedule_link + letterPost + id.toString()).get()
                val result = doc.getElementsByAttributeValue("class", "result")
                var html: String = result.toString().substringAfter("</h2>")
                while (html.contains("</h3>")) {
                    var weekday = html.substringAfter("<h3>").substringBefore("</h3>")
                    if (weekday == "Вне сетки расписания")
                        weekday = "Вне"
                    var pairs = html.substringAfter("</h3>").substringBefore("<h3>")
                    html = "<h3>" + html.substringAfter("</h3>").substringAfter("<h3>")

                    for (WeekDay in WeekDays.values()) {
                        if (WeekDay.name == weekday) {
                            days[WeekDay.ordinal] = parsePairs(pairs, WeekDay.ordinal)
                            for (pair in days[WeekDay.ordinal])
                            {
                                daysData.add(
                                    MyPairData(name ,WeekDay.ordinal, pair.time, pair.week,
                                        pair.type, pair.name, pair.teachers, pair.groups, pair.address, true
                                )
                                )
                            }
                        }
                    }
                }
                scheduleSaver.saveCash(daysData)
                Log.i(this.javaClass.simpleName, "Load schedule from internet successful")
            } catch (e: IOException) {
                scheduleSaver.deleteCash()
                //TODO : delete
                throw e
            } catch (e: Exception) {
                scheduleSaver.deleteCash()
                //Log.e(ContentValues.TAG, e.message.toString())
                throw e
            }
            //TODO: norm throw?
        }
    }

    //parse schedule one day for pairs
     suspend private fun parsePairs(daySheduleI: String, day : Int) : MutableList<MyPair>{
        //parse group and teachers in pair
        fun parseGroupOrTeacher(input : String) : String {
            var tmp = ""
            var dayScheduleTeachersOrGroups = input
            while (dayScheduleTeachersOrGroups != ""){
                tmp += dayScheduleTeachersOrGroups.substringAfter("\">").substringBefore("</a>") + ", "
                dayScheduleTeachersOrGroups = dayScheduleTeachersOrGroups.substringAfter("</a>")
            }
            tmp = tmp.substringBeforeLast(", ")
            return tmp
        }

        var daySchedule = daySheduleI.substringBeforeLast("</div>")
        var pairs : MutableList<MyPair> = mutableListOf()
        if (day == 6){
            var time = daySchedule.substringAfter("<h4>").substringBefore("</h4>").trim()
            var pairInTime = daySchedule.substringAfter("</h4>").substringBefore("<h4>")
            daySchedule = daySchedule.substringAfter(pairInTime)
            while (pairInTime.length > 10) {
                var type = pairInTime.substringAfter("<b>").substringBefore("</b>").trim()
                var name = pairInTime.substringAfter("–").substringBefore("<em>").trim()
                pairInTime = pairInTime.substringAfter("<em>")
                var address = pairInTime.substringAfter("–").substringBefore("</em>").trim()
                var teachers = "parseGroupOrTeacher(pairInTime.substringAfter(<a href).substringBefore(</span>)).trim()"
                var groups  = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                pairInTime = pairInTime.substringAfter("</a></span>").substringAfter("</div>")
                pairs.add(MyPair(time, 2, type, name, teachers, groups, address))
            }


        } else {
            while (daySchedule.length > 10) {
                var time = daySchedule.substringAfter("<h4>").substringBefore("</h4>").trim()
                var pairInTime = daySchedule.substringAfter("</h4>").substringBefore("<h4>")
                daySchedule = daySchedule.substringAfter(pairInTime)
                while (pairInTime.length > 10) {
                    var weekSting = ""
                    var week = 0
                    if (pairInTime.contains("class=\"up\"") || pairInTime.contains("class=\"dn\"")){
                        weekSting = pairInTime.substringAfter("title=\"").substringBefore("\"").trim()
                        pairInTime = pairInTime.substringAfter("</b>")
                        if (weekSting == "нижняя (четная)")
                            week = 0
                        else
                            week = 1
                    }
                    else {
                        week = 2
                    }
                    var type = pairInTime.substringAfter("<b>").substringBefore("</b>").trim()
                    var name = pairInTime.substringAfter("–").substringBefore("<em>").trim()
                    pairInTime = pairInTime.substringAfter("<em>")
                    var address = pairInTime.substringAfter("–").substringBefore("</em>").trim()
                    var teachers = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                    pairInTime = pairInTime.substringAfter("</a></span>")
                    var groups  = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                    pairInTime = pairInTime.substringAfter("</a></span>").substringAfter("</div>")
                    pairs.add(MyPair(time, week, type, name, teachers, groups, address))
                }
            }
        }
        return pairs
    }
}