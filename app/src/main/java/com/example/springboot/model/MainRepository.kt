package com.example.springboot.model

import androidx.lifecycle.LiveData
import com.example.springboot.model.api.ApiService
import com.example.springboot.model.local.MyDatabase
import com.example.springboot.model.local.student.Student
import com.example.springboot.model.local.student.StudentDao
import com.example.springboot.utils.BASE_URL
import com.example.springboot.utils.studentToJsonObject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(
    private val apiService: ApiService,
    private val studentDao : StudentDao
) {

    fun getAllStudents():LiveData<List<Student>>{
        return studentDao.getAllData()
    }

    // caching
    fun refreshData():Completable{
        return apiService
            .getAllStudents()
            .doOnSuccess {
                studentDao.insertAll(it)
            }.ignoreElement()
    }

    fun updateStudent(student: Student):Completable{
        return apiService
            .updateStudent(student.name, studentToJsonObject(student))
            .doOnComplete {
                studentDao.update(student)
            }
    }

    fun deleteStudent(studentName:String):Completable{
        return apiService
            .deleteStudent(studentName)
            .doOnComplete {
                studentDao.delete(studentName)
            }
    }

    fun insertStudent(student: Student):Completable{
        return apiService
            .insertStudent(studentToJsonObject(student))
            .doOnComplete {
                studentDao.insert(student)
            }
    }
}