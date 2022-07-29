package com.example.springboot.model.local.student

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
    @Query("SELECT * FROM student")
    fun getAllData() :LiveData<List<Student>>

    @Update
    fun update(student: Student)

    @Insert
    fun insert(student: Student)

    @Insert
    fun insertAll(listStudent :List<Student>)

    @Query("DELETE FROM student WHERE name = :studentName")
    fun delete(studentName : String)
}