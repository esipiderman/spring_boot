package com.example.springboot.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.springboot.model.MainRepository
import com.example.springboot.model.local.student.Student
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MainScreenViewModel(
    private val mainRepository: MainRepository
):ViewModel() {
    private lateinit var netDisposable: Disposable
    private val errorData = MutableLiveData<String>()

    init {
        mainRepository
            .refreshData()
            .subscribeOn(Schedulers.io())
            .subscribe(object :CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    netDisposable = d
                }

                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    errorData.postValue(e.message ?: "unknown error")
                }

            })
    }

    fun getAllData():LiveData<List<Student>>{
        return mainRepository.getAllStudents()
    }

    fun getErrorData():LiveData<String>{
        return errorData
    }

    fun deleteStudent(studentName: String):Completable{
        return mainRepository.deleteStudent(studentName)
    }

    override fun onCleared() {
        netDisposable.dispose()
        super.onCleared()
    }
}