package com.example.deadspace.data.database

import android.content.Context
import androidx.room.*

@Database(entities = [PairData::class, GroupAndTeacherData::class, MyDeadlinesData::class], version = 15)
abstract class MyDatabase : RoomDatabase() {
    abstract val myPairDAO : MyPairDAO
    abstract val myPairCashDAO : MyPairCashDAO
    abstract val myGroupAndTeacherDAO : MyGroupAndTeacherDAO
    abstract val myDeadlinesDAO: MyDeadlinesDAO
}


private lateinit var INSTANCEMyDatabase: MyDatabase

fun getDatabase(context: Context): MyDatabase {
    synchronized(MyDatabase::class) {
        if (!::INSTANCEMyDatabase.isInitialized) {
            INSTANCEMyDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCEMyDatabase
}