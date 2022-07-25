package com.example.springboot.utils

import com.example.springboot.model.Student
import com.google.gson.JsonObject

fun studentToJsonObject(student: Student):JsonObject{
    val jsonObject = JsonObject()
    jsonObject.addProperty("name", student.name)
    jsonObject.addProperty("course", student.course)
    jsonObject.addProperty("score", student.score)
    return jsonObject
}