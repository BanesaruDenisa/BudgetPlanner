package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class DataClass(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String = "",
    val dataDesc: String = "",
    val dataBudg: String = "",
    val dataImage: String = "",
    val dataDate: String = "",
    val userEmail: String = ""
)
