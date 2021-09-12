package com.suaideadspace.data.schedule

import com.suaideadspace.data.database.PairData

class ScheduleParser {

    enum class WeekDays {
        Понедельник, Вторник, Среда, Четверг, Пятница, Суббота, Вне
    }

    suspend fun parseSchedule(htmlString : String, itemId : Int, searchName : String) : List<PairData> {
        var schedule = htmlString

        var daysData: MutableList<PairData> = mutableListOf()
        while (schedule.contains("</h3>")) {
            var weekday = schedule.substringAfter("<h3>").substringBefore("</h3>")
            if (weekday == "Вне сетки расписания")
                weekday = "Вне"
            var pairs = schedule.substringAfter("</h3>").substringBefore("<h3>")

            schedule = "<h3>" + schedule.substringAfter("</h3>").substringAfter("<h3>")

            for (WeekDay in WeekDays.values()) {
                if (WeekDay.name == weekday) {
                    daysData.addAll(parsePairs(pairs, WeekDay.ordinal, itemId, searchName))
                }
            }
        }

        return daysData
    }

    //parse schedule one day for pairs
    suspend private fun parsePairs(dayScheduleInput: String, weekDay : Int, itemId : Int, searchName : String) : MutableList<PairData>{
        var daySchedule = dayScheduleInput.substringBeforeLast("</div>")
        var pairs : MutableList<PairData> = mutableListOf()

        while (daySchedule.length > 10) {

            var less = 0
            var startTime = "00:00"
            var endTime = "00:00"
            if (weekDay != 6) {
                var time = daySchedule.substringAfter("<h4>").substringBefore("</h4>")
                less = time.substringBefore(" ").toInt()
                startTime = time.substringAfter("(").substringBefore("–")
                time = time.substringAfter("–")
                endTime = time.substringBefore(")")
            }

            var pairInTime = daySchedule.substringAfter("</h4>").substringBefore("<h4>")
            daySchedule = daySchedule.substringAfter(pairInTime)

            while (pairInTime.length > 10) {

                var week = 0
                if (pairInTime.contains("class=\"up\"") || pairInTime.contains("class=\"dn\"")){
                    if (pairInTime.contains("class=\"up\""))
                        week = 1
                    pairInTime = pairInTime.substringAfter("</b>")
                }
                else {
                    week = 2
                }

                val type = pairInTime.substringAfter("<b>").substringBefore("</b>").trim()

                var disc = pairInTime.substringAfter("–").substringBefore("<em>").trim()
                //TODO: use strings
                if (disc == "Прикладная физическая культура (элективный модуль)")
                    disc = "Прикладная физическая культура"

                pairInTime = pairInTime.substringAfter("<em>")

                var build = pairInTime.substringAfter("–").substringBefore(",").trim()
                /*if (build.isEmpty())
                    build = "-"*/

                var room = pairInTime.substringAfter(",").substringBefore("</em>").trim()
                /*if (room.isEmpty())
                    room = "-"*/

                var teachers : String
                    if (pairInTime.contains("class=\"preps\"")) {
                        teachers = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()
                        pairInTime = pairInTime.substringAfter("</a></span>")
                    }
                    else
                        teachers = ""

                val groups  = parseGroupOrTeacher(pairInTime.substringAfter("<a href").substringBefore("</span>")).trim()

                pairInTime = pairInTime.substringAfter("</a></span>").substringAfter("</div>")

                pairs.add(
                    PairData(
                        ItemId = itemId * 10000 + week * 1000 + weekDay * 100 + less * 10,
                        Name = searchName,
                        Week = week,
                        Day = weekDay,
                        Less = less,
                        StartTime = startTime,
                        EndTime = endTime,
                        Build = build,
                        Rooms = room,
                        Disc = disc,
                        Type = type,
                        GroupsText = groups,
                        TeachersText = teachers,
                        isCash = true
                    )
                )
            }
        }

        return pairs
    }

    //parse group and teachers in pair
    private fun parseGroupOrTeacher(input : String) : String {
        var tmp = ""
        var dayScheduleTeachersOrGroups = input
        while (dayScheduleTeachersOrGroups != ""){
            tmp += dayScheduleTeachersOrGroups.substringAfter("\">").substringBefore("</a>") + ", "
            dayScheduleTeachersOrGroups = dayScheduleTeachersOrGroups.substringAfter("</a>")
        }
        tmp = tmp.substringBeforeLast(", ")
        return tmp
    }

}


private lateinit var INSTANCE: ScheduleParser

fun getScheduleParser(): ScheduleParser {
    synchronized(ScheduleParser::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleParser()
        }
    }
    return INSTANCE
}