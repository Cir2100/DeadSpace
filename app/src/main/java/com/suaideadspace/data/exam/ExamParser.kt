package com.suaideadspace.data.exam

import com.suaideadspace.data.database.ExamData

class ExamParser {

    suspend fun parseExams(htmlString : String, searchName : String) : List<ExamData> {
        var exams = htmlString

        var daysData: MutableList<ExamData> = mutableListOf()
        while (exams.contains("</h3>")) {
            var date = exams.substringAfter("<h3>").substringBefore("</h3>")
            if (date == "Вне сетки расписания")
                date = "Вне"
            var pairs = exams.substringAfter("</h3>").substringBefore("<h3>")

            exams = "<h3>" + exams.substringAfter("</h3>").substringAfter("<h3>")

            daysData.addAll(parsePairs(pairs, date, searchName))
        }

        return daysData
    }

    //parse schedule one day for pairs
    suspend private fun parsePairs(dayExamsInput: String, date : String, searchName : String) : MutableList<ExamData>{
        var dayExams = dayExamsInput.substringBeforeLast("</div>")
        var exams : MutableList<ExamData> = mutableListOf()

        while (dayExams.length > 10) {

            var change = dayExams.substringAfter("<h4>").substringBefore("</h4>")

            dayExams = dayExams.substringAfter("</h4>")

            var disc = dayExams.substringAfter("<span>").substringBefore("<em>").trim()
            //TODO: use strings
            if (disc == "Прикладная физическая культура (элективный модуль)")
                disc = "Прикладная физическая культура"

            dayExams = dayExams.substringAfter("<em>")

            var build = dayExams.substringAfter("–").substringBefore(" ").trim()
            /*if (build.isEmpty())
                build = "-"*/

            var room = dayExams.substringAfter(" ").substringBefore("</em>").trim()
            /*if (room.isEmpty())
                room = "-"*/

            var teachers : String
            if (dayExams.contains("class=\"preps\"")) {
                teachers = parseGroupOrTeacher(dayExams.substringAfter("<a href").substringBefore("</span>")).trim()
                dayExams = dayExams.substringAfter("</a></span>")
            }
            else
                teachers = ""

            val groups  = parseGroupOrTeacher(dayExams.substringAfter("<a href").substringBefore("</span>")).trim()

            dayExams = dayExams.substringAfter("</a></span>").substringAfter("</div>")

            exams.add(
                ExamData(
                    Date = date,
                    Change = change,
                    Build = build,
                    Rooms = room,
                    Disc = disc,
                    GroupsText = groups,
                    TeachersText = teachers
                )
            )

        }

        return exams
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

private lateinit var INSTANCE: ExamParser

fun getExamParser(): ExamParser {
    synchronized(ExamParser::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ExamParser()
        }
    }
    return INSTANCE
}