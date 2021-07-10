package com.example.deadspace.schedule

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Fts4
@Entity
data class MyPairData constructor(
    val group: String,
    val day : Int,
    val time : String,
    val week : Int,
    val type : String,
    val name : String,
    val teachers : String,
    val groups : String,
    val address : String,
    var isCash : Boolean //TODO: ??? val
)

@Dao
interface MyPairDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(myPairData: List<MyPairData>)

    @Query("SELECT * FROM MyPairData")
    suspend fun getAll(): List<MyPairData>

    //delete old cash
    @Query("DELETE FROM MyPairData WHERE isCash = 1")
    suspend fun deleteCash()


    /*@Query("SELECT * FROM MyPairData where isCash = 1")
    suspend fun getIsCash(): List<MyPairData>*/

    //load current cash
    @Query("SELECT * FROM MyPairData WHERE isCash = 1")
    suspend fun getCash(): MutableList<MyPairData>

    //load current cash
    @get:Query("SELECT * FROM MyPairData WHERE isCash = 1")
    val cashLiveData: LiveData<MyPairData>
}

@Database(entities = [MyPairData::class], version = 3)
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
