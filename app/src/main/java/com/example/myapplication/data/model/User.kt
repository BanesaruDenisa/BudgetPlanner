package com.example.myapplication.data.model

import androidx.room.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val password: String
)