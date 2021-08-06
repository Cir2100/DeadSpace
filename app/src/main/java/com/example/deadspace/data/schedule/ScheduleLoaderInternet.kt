package com.example.deadspace.data.schedule

import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class ScheduleLoaderInternet() {

    private val scheduleSaver = ScheduleSaver()

    private val myGroupAndTeacherDAO = getGroupAndTeacherDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    enum class WeekDays {
        Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Вне
    }

    private val scheduleLink : String = "https://rasp.guap.ru/"
    private val groupSearchLink : String = "?g="
    private val teacherSearchLink : String = "?p="

    suspend fun loadGroupAndTeacher() {
        withContext(Dispatchers.IO) {

            val items : MutableList<GroupAndTeacherData> = mutableListOf()

            val doc = Jsoup.connect(scheduleLink).get()
            val selecteds: Elements = doc.select("select")
            for (i in 0..1) {
                val options = selecteds[i].select("option")
                for (option in options) {
                    if (option.attr("value").toInt() != -1) {
                        if (i == 0)
                            items.add(GroupAndTeacherData(
                                ItemId = option.attr("value").toInt(),
                                Name = option.text(),
                                isGroup = true,
                                isUserHasOwn = false))
                        else if (i == 1)
                            items.add(
                                GroupAndTeacherData(
                                    ItemId = option.attr("value").toInt(),
                                    Name = option.text(),
                                    isGroup = false,
                                    isUserHasOwn = false)
                            )
                    }
                }
            }
            Log.i(this.javaClass.simpleName, "Load list groups and teachers successful")

            scheduleSaver.saveGroupList(items)
        }
    }

    //TODO fun loadWeekType()


    private suspend fun checkInput(name : String) : Pair<Int, String> {
        myGroupAndTeacherDAO.getAll().forEach {
            if (it.Name == name) {
                if (it.isGroup)
                    return Pair(it.ItemId, groupSearchLink)
                else
                    return Pair(it.ItemId, teacherSearchLink)
            }
        }
        throw Exception("Incorrect input")
    }

    //TODO : normal parse and refactor
    //find schedule group or teacher on rasp.guap.ru/?..
    suspend fun loadSchedule(name : String) {

        var days: MutableList<MutableList<MyPair>> = mutableListOf(
            mutableListOf(), mutableListOf(),
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())

        var daysData: MutableList<MyPairData> = mutableListOf()

        withContext(Dispatchers.IO) {

            //load schedule
            try {

                val pairSearch = checkInput(name)

                val doc = Jsoup.connect(scheduleLink + pairSearch.second + pairSearch.first.toString()).get()
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
                                    MyPairData(name.toInt() * 10000 + pair.week * 1000 + WeekDay.ordinal * 100 + pair.time.toInt() * 10 + 1,
                                        name ,WeekDay.ordinal, pair.time, pair.week,
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
               /// scheduleSaver.deleteCash()
                //TODO : delete
                throw e
            } catch (e: Exception) {
               // scheduleSaver.deleteCash()
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
            var time = "-"
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
                var time = daySchedule.substringAfter("<h4>").substringBefore("</h4>").substringBefore(" ")
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