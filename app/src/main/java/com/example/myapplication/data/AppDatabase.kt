package com.example.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.dao.DataClassDao
import com.example.myapplication.data.dao.UserDao
import com.example.myapplication.data.model.DataClass
import com.example.myapplication.data.model.User

@Database(entities = [User::class, DataClass::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dataClassDao(): DataClassDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_planner_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
