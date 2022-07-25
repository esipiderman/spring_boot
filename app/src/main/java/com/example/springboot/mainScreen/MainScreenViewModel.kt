package com.example.springboot.mainScreen

import com.example.springboot.model.MainRepository
import com.example.springboot.model.Student
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class MainScreenViewModel(
    private val mainRepository: MainRepository
) {
    val progressbarSubject = BehaviorSubject.create<Boolean>()

    fun getAllStudent():Single<List<Student>>{
        progressbarSubject.onNext(true)
        return mainRepository
            .getAllStudents()
            .doFinally {
                progressbarSubject.onNext(false)
            }
    }

    fun deleteStudent(studentName: String):Completable{
        return mainRepository.deleteStudent(studentName)
    }
}