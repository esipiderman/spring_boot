package com.example.springboot.apiManager

import com.example.springboot.recyclerMain.Student
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/student")
    fun getAllStudents(): Single<List<Student>>

    @POST("/student")
    fun insertStudent(@Body body: JsonObject): Single<String>

    @PUT("/student/updating{name}")
    fun updateStudent(@Path("name") name: String, @Body data: JsonObject): Call<String>

    @DELETE("/student/deleting{name}")
    fun deleteStudent(@Path("name")name:String):Call<String>
}