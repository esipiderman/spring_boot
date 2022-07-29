package com.example.springboot.addStudent

import androidx.lifecycle.ViewModel
import com.example.springboot.model.MainRepository
import com.example.springboot.model.local.student.Student
import io.reactivex.Completable

class AddStudentViewModel(private val mainRepository: MainRepository):ViewModel() {

    fun insertStudent(student: Student):Completable{
        return mainRepository.insertStudent(student)
    }

    fun updateStudent(student: Student):Completable{
        return mainRepository.updateStudent(student)
    }

}