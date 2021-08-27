package com.example.deadspace.data.exam

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.deadspace.DeadSpace
import com.example.deadspace.R
import com.example.deadspace.data.database.GroupAndTeacherData
import com.example.deadspace.data.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class ExamLoaderInternet {

    private val examSaver = getExamSaver()
    private val examParser = getExamParser()

    private val myGroupAndTeacherDAO = getDatabase(DeadSpace.appContext).myGroupAndTeacherDAO

    private val res =  DeadSpace.appContext.resources

    private lateinit var html : Document

    suspend fun updateGroupAndTeacher() : String? {
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
                                        isGroup = true,
                                        isSchedule = false
                                    )
                                )
                            else if (i == 1)
                                items.add(
                                    GroupAndTeacherData(
                                        ItemId = option.attr("value").toInt(),
                                        Name = option.text(),
                                        isGroup = false,
                                        isSchedule = false
                                    )
                                )
                        }
                    }
                }
                Log.i(this.javaClass.simpleName, "Load list groups and teachers successful")

                examSaver.saveGroupList(items)

                return@withContext null
            } catch (e: IOException) {
                return@withContext e.message.toString()
            } catch (e: Exception) {
                return@withContext e.message.toString()
            }

        }
    }

    private suspend fun checkInput(name : String) : Pair<Int, String> {
        myGroupAndTeacherDAO.getAllExams().forEach {
            if (it.Name == name) {
                return if (it.isGroup)
                    Pair(it.ItemId, examGroupSearchLink)
                else
                    Pair(it.ItemId, examTeacherSearchLink)
            }
        }
        throw Exception(res.getString(R.string.incorrect_input))
    }

    //find schedule group or teacher on rasp.guap.ru/?..
    suspend fun loadExams(searchName : String) : String? {

        return withContext(Dispatchers.IO) {

            if (!isOnline())
                return@withContext res.getString(R.string.no_internet_connection)

            //load schedule
            try {

                val pairSearch = checkInput(searchName)

                val doc = Jsoup.connect(examLink + pairSearch.second + pairSearch.first.toString()).get()
                val result = doc.getElementsByAttributeValue("class", "result")

                examSaver.saveCash(examParser.parseExams(result.toString().substringAfter("</h2>"), searchName))

                Log.i(this.javaClass.simpleName, "Load exams from internet successful")
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
                Jsoup.connect(examLink).get()
        }
    }

}

private lateinit var INSTANCE: ExamLoaderInternet

fun getExamLoaderInternet(): ExamLoaderInternet {
    synchronized(ExamLoaderInternet::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = ExamLoaderInternet()
        }
    }
    return INSTANCE
}