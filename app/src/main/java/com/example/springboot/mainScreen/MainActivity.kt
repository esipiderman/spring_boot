package com.example.springboot.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.springboot.addStudent.MainActivity2
import com.example.springboot.databinding.ActivityMainBinding
import com.example.springboot.model.MainRepository
import com.example.springboot.model.Student
import com.example.springboot.utils.asyncRequest
import com.example.springboot.utils.showToast
import io.reactivex.CompletableObserver
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), RecyclerAdapter.StudentEvent {
    private lateinit var binding: ActivityMainBinding
    lateinit var recyclerAdapter: RecyclerAdapter
    private val compositeDisposable = CompositeDisposable()
    lateinit var mainScreenViewModel: MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainScreenViewModel = MainScreenViewModel(MainRepository())

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        mainScreenViewModel
            .getAllStudent()
            .asyncRequest()
            .subscribe(object : SingleObserver<List<Student>> {
                override fun onSubscribe(d: Disposable) {

                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(ArrayList(t))
                }

                override fun onError(e: Throwable) {
                    showToast("error" + (e.message ?: "null"))
                }

            })

        compositeDisposable.add(
            mainScreenViewModel
                .progressbarSubject
                .subscribe {
                    runOnUiThread {
                        if (it) {
                            binding.progressMain.visibility = View.VISIBLE
                            binding.recyclerMain.visibility = View.INVISIBLE
                        } else {
                            binding.progressMain.visibility = View.INVISIBLE
                            binding.recyclerMain.visibility = View.VISIBLE
                        }
                    }
                }
        )
    }

    private fun setDataToRecycler(data: ArrayList<Student>) {
        recyclerAdapter = RecyclerAdapter(data, this)
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
            deleteDataFromServer(student, position)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteDataFromServer(student: Student, position: Int) {
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
        recyclerAdapter.removeItem(student, position)
    }
}