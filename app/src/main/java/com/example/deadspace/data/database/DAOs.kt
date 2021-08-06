package com.example.deadspace.data.database

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

    //delete user schedule
    @Query("DELETE FROM MyPairData WHERE isCash = 0 AND `group` LIKE :group" +
            " AND (week = :weekType OR week = 2) AND day = :weekDay AND time LIKE :time")
    suspend fun deleteUserPair(group: String, weekType : Int, weekDay : Int, time : String)

    //load users input
    @Query("SELECT * FROM MyPairData WHERE isCash = 0 AND `group` LIKE :name")
    suspend fun getUserData(name: String): MutableList<MyPairData>

    //clear database
    /*@Query("DELETE FROM MyPairData")
    suspend fun deleteAll()*/

}

@Dao
interface MyPairCashDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myPairData: List<PairData>)

    //delete old cash
    @Query("DELETE FROM PairData")
    suspend fun deleteCash()

    //load current cash
    @Query("SELECT * FROM PairData")
    suspend fun getCash(): MutableList<PairData>

    //TODO : delete?
    //load current cash
    @get:Query("SELECT * FROM PairData")
    val allCash: LiveData<List<PairData>>

    //load current day cash
    @Query("SELECT * FROM PairData WHERE (week = :weekType OR week = 2) AND day = :weekDay")
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

}