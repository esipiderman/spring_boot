package com.example.springboot.model

import com.example.springboot.utils.BASE_URL
import com.example.springboot.utils.studentToJsonObject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun getAllStudents():Single<List<Student>>{
        return apiService.getAllStudents()
    }

    fun updateStudent(student: Student):Completable{
        return apiService.updateStudent(student.name, studentToJsonObject(student))
    }

    fun deleteStudent(studentName:String):Completable{
        return apiService.deleteStudent(studentName)
    }

    fun insertStudent(student: Student):Completable{
        return apiService.insertStudent(studentToJsonObject(student))
    }
}