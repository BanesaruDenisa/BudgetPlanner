package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun authenticate(email: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)
}