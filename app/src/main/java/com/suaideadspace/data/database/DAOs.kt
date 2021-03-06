package com.suaideadspace.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MyPairDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pairData: List<PairData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(pairData: PairData) : Long

    @Query("SELECT * FROM PairData")
    suspend fun getAll(): List<PairData>

    //delete user shcedule
    @Query("DELETE FROM PairData WHERE `Name` LIKE :name AND isCash = 0")
    suspend fun deleteUserSchedule(name : String) : Int

    //delete pair
    @Delete
    suspend fun deleteUserPair(pair: PairData)

    //load users input
    @Query("SELECT * FROM PairData WHERE `Name` LIKE :name AND isCash = 0")
    suspend fun getUserSchedule(name: String): List<PairData>

    //clear database
    @Query("DELETE FROM PairData")
    suspend fun deleteAll()

}

@Dao
interface MyPairCashDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pairCashData: List<PairData>)

    //delete old cash
    @Query("DELETE FROM PairData WHERE isCash = 1")
    suspend fun deleteCash()

    //load current cash
    @Query("SELECT * FROM PairData WHERE isCash = 1")
    suspend fun getCash(): MutableList<PairData>

    //get days without Sunday
    @Query("SELECT * FROM PairData WHERE isCash = 1 AND Day != 6")
    suspend fun getCashWorkDays(): MutableList<PairData>

    //TODO : delete?
    //load current cash
    @get:Query("SELECT * FROM PairData WHERE isCash = 1")
    val allCash: LiveData<List<PairData>>

    //load current day cash
    @Query("SELECT * FROM PairData WHERE isCash = 1 AND " +
            "(week = :weekType OR week = 2) AND day = :weekDay")
    suspend fun getDayCash(weekType : Int, weekDay : Int) : List<PairData>

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

    @Query("SELECT count(*) FROM MyDeadlinesData")
    suspend fun getCountDeadlines(): Int

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

    @Query("SELECT * FROM GroupAndTeacherData WHERE isSchedule == 1")
    suspend fun getAllSchedule() : List<GroupAndTeacherData>

    @Query("SELECT * FROM GroupAndTeacherData WHERE isSchedule == 0")
    suspend fun getAllExams() : List<GroupAndTeacherData>

    @Query("SELECT * FROM GroupAndTeacherData WHERE Name = :name")
    suspend fun getOne(name : String) : GroupAndTeacherData

    @Update
    suspend fun updateOne(groupAndTeacherData: GroupAndTeacherData)

}

@Dao
interface MyExamDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ExamData>)

    //delete old cash
    @Query("DELETE FROM ExamData")
    suspend fun deleteCash()

    @get:Query("SELECT * FROM ExamData")
    val allExams: LiveData<List<ExamData>>

}