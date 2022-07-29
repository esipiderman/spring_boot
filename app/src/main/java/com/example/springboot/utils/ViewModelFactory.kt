package com.example.springboot.utils

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.springboot.addStudent.AddStudentViewModel
import com.example.springboot.mainScreen.MainScreenViewModel
import com.example.springboot.model.MainRepository

class MainViewFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainScreenViewModel(mainRepository) as T
    }

}

class AddStudentViewFactory(private val mainRepository: MainRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddStudentViewModel(mainRepository) as T
    }

}