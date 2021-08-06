package com.example.deadspace.data.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyPairDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myPairData: List<MyPairData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(myPairData: MyPairData)

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(myDeadlinesData: MyDeadlinesData)

    @Query("DELETE FROM MyDeadlinesData WHERE id = :id")
    suspend fun deleteOne(id : Int)

    @get:Query("SELECT * FROM MyDeadlinesData")
    val allDeadlines: LiveData<List<MyDeadlinesData>>

    @get:Query("SELECT count(*) FROM MyDeadlinesData")
    val countDeadlines: LiveData<Int>

    @Query("SELECT * FROM MyDeadlinesData")
    suspend fun getAllDeadlines(): List<MyDeadlinesData>

    @Query("SELECT * FROM MyDeadlinesData WHERE id = :id")
    suspend fun getOne(id : Int) : MyDeadlinesData

    @Update
    suspend fun updateOne(myDeadlinesData: MyDeadlinesData)

    //use this for clear database
    @Query("DELETE FROM MyDeadlinesData")
    suspend fun deleteAll()


}

@Dao
interface MyGroupAndTeacherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GroupAndTeacherData>)


    @Query("SELECT * FROM GroupAndTeacherData")
    suspend fun getAll() : List<GroupAndTeacherData>

    @Query("SELECT Name FROM GroupAndTeacherData WHERE Name LIKE :name")
    suspend fun getQuerySuggestions(name : String): List<String>

}