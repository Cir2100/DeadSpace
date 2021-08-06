package com.example.deadspace.data.database

import android.content.Context
import androidx.room.*

@Database(entities = [MyPairData::class], version = 11)
abstract class MyPairDatabase : RoomDatabase() {
    abstract val myPairDAO : MyPairDAO
}

@Database(entities = [MyPairData::class], version = 1)
abstract class MyPairCashDatabase : RoomDatabase() {
    abstract val myPairDAO : MyPairDAO
}

@Database(entities = [GroupAndTeacherData::class], version = 3)
abstract class MyGroupAndTeacherDatabase : RoomDatabase() {
    abstract val myGroupAndTeacherDAO : MyGroupAndTeacherDAO
}

@Database(entities = [MyDeadlinesData::class], version = 1)
abstract class MyDeadlineDatabase : RoomDatabase() {
    abstract val myDeadlinesDAO : MyDeadlinesDAO
}

private lateinit var INSTANCEMyPairDatabase: MyPairDatabase
private lateinit var INSTANCEMyPairCashDatabase: MyPairCashDatabase
private lateinit var INSTANCEMyGroupAndTeacherDatabase: MyGroupAndTeacherDatabase
private lateinit var INSTANCEMyDeadlineDatabase: MyDeadlineDatabase

fun getPairDatabase(context: Context): MyPairDatabase {
    synchronized(MyPairDatabase::class) {
        if (!::INSTANCEMyPairDatabase.isInitialized) {
            INSTANCEMyPairDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyPairDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCEMyPairDatabase
}

fun getPairCashDatabase(context: Context): MyPairCashDatabase {
    synchronized(MyPairDatabase::class) {
        if (!::INSTANCEMyPairCashDatabase.isInitialized) {
            INSTANCEMyPairCashDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyPairCashDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCEMyPairCashDatabase
}

fun getGroupAndTeacherDatabase(context: Context): MyGroupAndTeacherDatabase {
    synchronized(MyPairDatabase::class) {
        if (!::INSTANCEMyGroupAndTeacherDatabase.isInitialized) {
            INSTANCEMyGroupAndTeacherDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyGroupAndTeacherDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCEMyGroupAndTeacherDatabase
}

fun getDeadlineDatabase(context: Context): MyDeadlineDatabase {
    synchronized(MyPairDatabase::class) {
        if (!::INSTANCEMyDeadlineDatabase.isInitialized) {
            INSTANCEMyDeadlineDatabase = Room
                .databaseBuilder(
                    context.applicationContext,
                    MyDeadlineDatabase::class.java,
                    "titles_db"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCEMyDeadlineDatabase
}
