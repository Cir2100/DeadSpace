package com.example.deadspace.schedule

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Fts4
@Entity
data class MyPairData constructor(
    val time : String,
    val week : Int,
    val type : String,
    val name : String,
    val teachers : String,
    val groups : String,
    val address : String,
    val isCash : Boolean
)

@Dao
interface MyPairDao {
    @Insert
    suspend fun insertAll(vararg myPairData: MyPairData)

    @Delete
    suspend fun delete(myPairData: MyPairData)


    @Query("SELECT * FROM MyPairData")
    suspend fun getAll(): List<MyPairData>

    /*@Query("SELECT * FROM MyPairData where isCash = 1")
    suspend fun getIsCash(): List<MyPairData>*/

    @get:Query("select * from MyPairData where isCash = 1")
    val cashLiveData: LiveData<MyPairData>
}

@Database(entities = [MyPairData::class], version = 1)
abstract class MyPairDatabase : RoomDatabase() {
    //abstract fun myPairDao(): MyPairDao
    abstract val myPairDao : MyPairDao
}

private lateinit var INSTANCE: MyPairDatabase

/**
 * Instantiate a database from a context.
 */
//TODO : learn and using
fun getDatabase(context: Context): MyPairDatabase {
    synchronized(MyPairDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyPairDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
