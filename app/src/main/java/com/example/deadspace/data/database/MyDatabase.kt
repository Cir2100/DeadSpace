package com.example.deadspace.data.database

import android.content.Context
import androidx.room.*

@Database(entities = [MyPairData::class, MyDeadlinesData::class], version = 6)
abstract class MyPairDatabase : RoomDatabase() {
    abstract val myPairDAO : MyPairDAO
    abstract val myDeadlinesDAO : MyDeadlinesDAO
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