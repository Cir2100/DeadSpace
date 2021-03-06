package com.suaideadspace.data.schedule

import android.util.Log

class PairTime {
    //TODO get from SUAI
    private val pairTimeUp = listOf(
        //1
        Pair("9:30", "11:00"),
        //2
        Pair("11:10", "12:40"),
        //3
        Pair("13:00", "14:30"),
        //4
        Pair("15:00", "16:30"),
        //5
        Pair("16:40", "18:10"),
        //6
        Pair("18:30", "20:00"),
        //7
        Pair("20:10", "21:40")
    )

    private val pairTimeMid = listOf(
        //1
        Pair("9:20", "10:50"),
        //2
        Pair("11:00", "12:30"),
        //3
        Pair("13:00", "14:30"),
        //4
        Pair("14:40", "16:10"),
        //5
        Pair("16:30", "18:00")
    )

    private val pairTimeUpInt = toTimeList(pairTimeUp)
    private val pairTimeMidInt = toTimeList(pairTimeMid)

    suspend fun getPairTime(startTime : String) : List<Pair<Int, Int>> {

        pairTimeUpInt.forEach {
            if (toTime(startTime) == it.first)
                return pairTimeUpInt
        }

        pairTimeMidInt.forEach {
            if (toTime(startTime) == it.first)
                return pairTimeMidInt
        }

        return listOf()
    }

    /*suspend fun getPairTime(less : Int) : Pair<Int, Int> {

        val cash = myPairCashDAO.getCash()

        if (cash.size > 0) {
            val pair = cash[0]

            for (i in pairTimeUpInt.indices) {
                if (toTime(pair.StartTime) == pairTimeUpInt[i].first && less - 1 == i)
                    return pairTimeUpInt[i]
            }

            for (i in pairTimeMidInt.indices) {
                if (toTime(pair.StartTime) == pairTimeMidInt[i].first && less - 1 == i)
                    return pairTimeMidInt[i]
            }
        }
        //this don't be executed
        return pairTimeUpInt[0]
    }*/

    fun getPairTime(less : Int, startTime : String) : Pair<String, String> {

        for (i in pairTimeUpInt.indices) {
            if (startTime == pairTimeUp[i].first){
                return pairTimeUp[less - 1]
            }

        }

        for (i in pairTimeMidInt.indices) {
            if (startTime == pairTimeMid[i].first)
                return pairTimeMid[less - 1]
        }

        return Pair("00:00","00:00")
    }


    fun toTime(time : String) : Int {
        return time.substringBefore(":").toInt() * 60 +
                time.substringAfter(":").toInt()
    }

    private fun toTimeList(list : List<Pair<String, String>>) : List<Pair<Int, Int>> {
        val listInt : MutableList<Pair<Int, Int>> = mutableListOf()
        list.forEach {
            listInt.add(Pair(toTime(it.first), toTime(it.second)))
        }
        return listInt
    }

}

private lateinit var INSTANCE: PairTime

fun getSchedulePairTime(): PairTime {
    synchronized(PairTime::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = PairTime()
        }
    }
    return INSTANCE
}