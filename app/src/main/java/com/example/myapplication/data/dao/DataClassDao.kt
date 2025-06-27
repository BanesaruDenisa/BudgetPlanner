package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.model.DataClass

@Dao
interface DataClassDao {
    @Insert
    fun insert(data: DataClass)

    @Query("SELECT * FROM data_table")
    fun getAll(): List<DataClass>

    @Query("SELECT * FROM data_table WHERE userEmail = :email")
    fun getAllForUser(email: String): List<DataClass>

    @Query("SELECT * FROM data_table WHERE id = :id")
    fun getById(id: Int): DataClass?

    @Update
    fun update(data: DataClass)

    @Delete
    fun delete(data: DataClass)
}
