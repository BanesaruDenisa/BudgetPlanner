package com.example.myapplication

import androidx.room.*
import android.content.Context

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val password: String
)