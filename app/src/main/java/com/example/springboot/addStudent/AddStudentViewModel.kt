package com.example.springboot.addStudent

import com.example.springboot.model.MainRepository
import com.example.springboot.model.Student
import io.reactivex.Completable

class AddStudentViewModel(private val mainRepository: MainRepository) {

    fun insertStudent(student: Student):Completable{
        return mainRepository.insertStudent(student)
    }

    fun updateStudent(student: Student):Completable{
        return mainRepository.updateStudent(student)
    }

}