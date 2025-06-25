package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DataClassDao {
    @Insert
    fun insert(data: DataClass)

    @Query("SELECT * FROM data_table")
    fun getAll(): List<DataClass>

    @Query("SELECT * FROM data_table WHERE id = :id")
    fun getById(id: Int): DataClass?

    @Update
    fun update(data: DataClass)

    @Delete
    fun delete(data: DataClass)
}