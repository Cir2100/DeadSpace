package com.example.deadspace.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDate

@Dao
interface MyPairDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myPairData: List<MyPairData>)

    @Query("SELECT * FROM MyPairData")
    suspend fun getAll(): List<MyPairData>

    //delete old cash
    @Query("DELETE FROM MyPairData WHERE isCash = 1")
    suspend fun deleteCash()

    //delete user schedule
    @Query("DELETE FROM MyPairData WHERE isCash = 0 AND `group` LIKE :group" +
            " AND (week = :weekType OR week = 2) AND day = :weekDay AND time LIKE :time")
    suspend fun deleteUserPair(group: String, weekType : Int, weekDay : Int, time : String)

    //load users input
    @Query("SELECT * FROM MyPairData WHERE isCash = 0 AND `group` LIKE :name")
    suspend fun getUserData(name: String): MutableList<MyPairData>

    //load current cash
    @Query("SELECT * FROM MyPairData WHERE isCash = 1")
    suspend fun getCash(): MutableList<MyPairData>

    //TODO : delete?
    //load current cash
    @get:Query("SELECT * FROM MyPairData WHERE isCash = 1")
    val allCash: LiveData<List<MyPairData>>

    //clear database
    @Query("DELETE FROM MyPairData")
    suspend fun deleteAll()

    //load current day cash
    @Query("SELECT * FROM MyPairData WHERE isCash = 1 AND (week = :weekType OR week = 2) AND day = :weekDay")
    suspend fun getDayCash(weekType : Int, weekDay : Int) : List<MyPairData>
}

@Dao
interface MyDeadlinesDAO {

    @Insert
    suspend fun insertOne(myDeadlinesData: MyDeadlinesData)

    @Query("DELETE FROM MyDeadlinesData WHERE title LIKE :title AND discipline LIKE :discipline AND lastDate = :lastDate")
    suspend fun deleteOne(title : String, discipline : String, lastDate : String)

    @get:Query("SELECT * FROM MyDeadlinesData")
    val allDeadlines: LiveData<List<MyDeadlinesData>>

    @Query("SELECT * FROM MyDeadlinesData")
    suspend fun getAllDeadlines(): List<MyDeadlinesData>
}