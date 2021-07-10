package com.example.deadspace.schedule

import android.content.Context
import androidx.room.*

@Fts4
@Entity
data class MyPairData constructor(
    val name: String
)

@Dao
interface MyPairDao {
    @Insert
    fun insertAll(vararg myPairData: MyPairData)

    @Delete
    fun delete(myPairData: MyPairData)

    @Query("SELECT * FROM MyPairData")
    fun getAll(): List<MyPairData>
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
