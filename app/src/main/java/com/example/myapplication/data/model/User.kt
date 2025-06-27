package com.example.myapplication.data.model

import androidx.room.*
import android.content.Context

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val password: String
)