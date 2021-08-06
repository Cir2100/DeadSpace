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
    suspend fun loadSchedule(searchName : String) {

        val scheduleParser = ScheduleParser()

        withContext(Dispatchers.IO) {

            //load schedule
            try {

                val pairSearch = checkInput(searchName)

                val doc = Jsoup.connect(scheduleLink + pairSearch.second + pairSearch.first.toString()).get()
                val result = doc.getElementsByAttributeValue("class", "result")

                scheduleSaver.saveCash(scheduleParser.parseSchedule(result.toString().substringAfter("</h2>"), pairSearch.first, searchName))

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


}