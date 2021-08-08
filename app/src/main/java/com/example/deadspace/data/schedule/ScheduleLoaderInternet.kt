package com.example.deadspace.data.schedule

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class ScheduleLoaderInternet {

    private val scheduleSaver = getScheduleSaver()
    private val scheduleParser = getScheduleParser()

    private val myGroupAndTeacherDAO = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    private val res =  DeadSpace.appContext.resources

    private lateinit var html : Document
    private var week : Boolean? = null

    suspend fun loadGroupAndTeacher() : String? {
        return withContext(Dispatchers.IO) {

            if (!isOnline())
                return@withContext res.getString(R.string.no_internet_connection)

            val items : MutableList<GroupAndTeacherData> = mutableListOf()
            try {
                val doc = getHTML()
                val selecteds: Elements = doc.select("select")
                for (i in 0..1) {
                    val options = selecteds[i].select("option")
                    for (option in options) {
                        if (option.attr("value").toInt() != -1) {
                            if (i == 0)
                                items.add(
                                    GroupAndTeacherData(
                                        ItemId = option.attr("value").toInt(),
                                        Name = option.text(),
                                        isGroup = true
                                    )
                                )
                            else if (i == 1)
                                items.add(
                                    GroupAndTeacherData(
                                        ItemId = option.attr("value").toInt(),
                                        Name = option.text(),
                                        isGroup = false
                                    )
                                )
                        }
                    }
                }
                Log.i(this.javaClass.simpleName, "Load list groups and teachers successful")

                scheduleSaver.saveGroupList(items)

                return@withContext null
            } catch (e: IOException) {
                return@withContext e.message.toString()
            } catch (e: Exception) {
                return@withContext e.message.toString()
            }

        }
    }

    suspend fun getWeekType() : Boolean {
        if (week == null)
            week = loadWeekType()
        return week!!
    }

    private suspend fun loadWeekType() : Boolean {
        return withContext(Dispatchers.IO) {

            if (!isOnline())
                throw Exception(res.getString(R.string.no_internet_connection))
            try {

                val doc = getHTML()
                val semInfo = doc.select("div[class*=rasp]").select("em")[0].toString()

                Log.i(this.javaClass.simpleName,"Load weekType successful")

                return@withContext semInfo.contains("up")
            }catch (e: IOException) {
                throw e
            } catch (e: Exception) {
                throw e
            }
        }

    }


    private suspend fun checkInput(name : String) : Pair<Int, String> {
        myGroupAndTeacherDAO.getAll().forEach {
            if (it.Name == name) {
                return if (it.isGroup)
                    Pair(it.ItemId, groupSearchLink)
                else
                    Pair(it.ItemId, teacherSearchLink)
            }
        }
        throw Exception("Incorrect input")
    }

    //TODO : normal parse and refactor
    //find schedule group or teacher on rasp.guap.ru/?..
    suspend fun loadSchedule(searchName : String) : String? {

        return withContext(Dispatchers.IO) {

            if (!isOnline())
                return@withContext res.getString(R.string.no_internet_connection)

            //load schedule
            try {

                val pairSearch = checkInput(searchName)

                val doc = Jsoup.connect(scheduleLink + pairSearch.second + pairSearch.first.toString()).get()
                val result = doc.getElementsByAttributeValue("class", "result")

                scheduleSaver.saveCash(scheduleParser.parseSchedule(result.toString().substringAfter("</h2>"), pairSearch.first, searchName))

                Log.i(this.javaClass.simpleName, "Load schedule from internet successful")
                return@withContext null
            } catch (e: IOException) {
               return@withContext e.message.toString()
            } catch (e: Exception) {
                return@withContext e.message.toString()
            }
        }
    }

    private suspend fun isOnline(): Boolean {
        val connectivityManager =
            DeadSpace.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private suspend fun getHTML() : Document {
        return withContext(Dispatchers.IO) {
            return@withContext if (::html.isInitialized)
                html
            else
                Jsoup.connect(scheduleLink).get()
        }
    }

}

private lateinit var INSTANCE: ScheduleLoaderInternet

fun getScheduleLoaderInternet(): ScheduleLoaderInternet {
    synchronized(ScheduleLoaderInternet::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ScheduleLoaderInternet()
        }
    }
    return INSTANCE
}