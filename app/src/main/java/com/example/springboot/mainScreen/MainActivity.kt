package com.example.springboot.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.springboot.addStudent.MainActivity2
import com.example.springboot.databinding.ActivityMainBinding
import com.example.springboot.model.MainRepository
import com.example.springboot.model.local.MyDatabase
import com.example.springboot.model.local.student.Student
import com.example.springboot.model.local.student.StudentDao
import com.example.springboot.utils.ApiServiceSingleton
import com.example.springboot.utils.MainViewFactory
import com.example.springboot.utils.asyncRequest
import com.example.springboot.utils.showToast
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainActivity : AppCompatActivity(), RecyclerAdapter.StudentEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: RecyclerAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainScreenViewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()

        mainScreenViewModel = ViewModelProvider(
            this,
            MainViewFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(MainScreenViewModel::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        mainScreenViewModel.getAllData().observe(this){
            recyclerAdapter.refreshData(it)
        }

        mainScreenViewModel.getErrorData().observe(this){
            Log.e("testLog", it)
        }
    }

    private fun initRecycler() {
        recyclerAdapter = RecyclerAdapter(arrayListOf(), this)
        binding.recyclerMain.adapter = recyclerAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("updateStudent", student)
        startActivity(intent)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setContentText("Delete this item?")
            .setCancelText("cancel")
            .setConfirmText("confirm")

        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {
            deleteDataFromServer(student)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteDataFromServer(student: Student) {
        mainScreenViewModel
            .deleteStudent(student.name)
            .asyncRequest()
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    showToast("student removed.")
                }

                override fun onError(e: Throwable) {
                    showToast("error" + (e.message ?: "null"))
                }

            })
    }
}
