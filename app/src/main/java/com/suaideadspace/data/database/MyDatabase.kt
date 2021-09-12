package com.suaideadspace.data.database

import android.content.Context
import androidx.room.*

@Database(entities = [PairData::class, GroupAndTeacherData::class, MyDeadlinesData::class, ExamData::class], version = 18)
abstract class MyDatabase : RoomDatabase() {
    abstract val myPairDAO : MyPairDAO
    abstract val myPairCashDAO : MyPairCashDAO
    abstract val myGroupAndTeacherDAO : MyGroupAndTeacherDAO
    abstract val myDeadlinesDAO: MyDeadlinesDAO
    abstract val myExamDAO: MyExamDAO
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