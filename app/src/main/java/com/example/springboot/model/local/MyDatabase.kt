package com.example.springboot.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.springboot.model.local.student.Student
import com.example.springboot.model.local.student.StudentDao

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract val studentDao: StudentDao

    companion object {

        @Volatile
        private var dataBase: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {

            synchronized(this) {
                if (dataBase == null) {

                    dataBase = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "myDatabase.db"
                    )
                        .build()

                }

                return dataBase!!
            }


        }
    }


}