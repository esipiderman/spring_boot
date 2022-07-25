package com.example.springboot.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.springboot.databinding.ActivityMain2Binding
import com.example.springboot.model.MainRepository
import com.example.springboot.model.Student
import com.example.springboot.utils.asyncRequest
import com.example.springboot.utils.showToast
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    private val compositeDisposable = CompositeDisposable()
    private val addStudentViewModel = AddStudentViewModel(MainRepository())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAddStudent.toolbar)

        binding.toolbarAddStudent.toolbar.title = "Add New Student"

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.edtFirstName.requestFocus()

        var isUpdating = intent.getParcelableExtra<Student>("updateStudent") != null

        if (isUpdating){
            logicUpdateStudent()
        }

        binding.btnDone.setOnClickListener {
            if (isUpdating){
                updateStudent()
            }else{
                insertStudent()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun logicUpdateStudent() {
        val dataFromIntent = intent.getParcelableExtra<Student>("updateStudent")!!
        binding.edtCourse.setText(dataFromIntent.course)
        binding.edtScore.setText(dataFromIntent.score.toString())

        val splitedName = dataFromIntent.name.split(" ")
        binding.edtFirstName.setText(splitedName[0])
        binding.edtLastName.setText(splitedName[1])
    }
    private fun updateStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (firstName.isNotEmpty()){
            if (lastName.isNotEmpty()){
                if (course.isNotEmpty()){
                    if (score.isNotEmpty()){
                        addStudentViewModel
                            .updateStudent(
                                Student(
                                    "$firstName $lastName",
                                    course,
                                    score.toInt()
                                )
                            )
                            .asyncRequest()
                            .subscribe(object :CompletableObserver{
                                override fun onSubscribe(d: Disposable) {
                                    compositeDisposable.add(d)
                                }

                                override fun onComplete() {
                                    showToast("student updated")
                                    onBackPressed()
                                }

                                override fun onError(e: Throwable) {
                                    showToast("error" + (e.message ?: "null"))
                                }

                            })
                    }else{
                        showToast("write your score")
                    }
                }else{
                    showToast("write your course")
                }
            }else{
                showToast("write your last name")
            }
        }else{
            showToast("write your first name")
        }
    }
    private fun insertStudent()  {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (firstName.isNotEmpty()){
            if (lastName.isNotEmpty()){
                if (course.isNotEmpty()){
                    if (score.isNotEmpty()){
                        addStudentViewModel
                            .insertStudent(
                                Student(
                                    "$firstName $lastName",
                                    course,
                                    score.toInt()
                                )
                            )
                            .asyncRequest()
                            .subscribe(object :CompletableObserver{
                                override fun onSubscribe(d: Disposable) {
                                    compositeDisposable.add(d)
                                }

                                override fun onComplete() {
                                    showToast("student inserted")
                                    onBackPressed()
                                }

                                override fun onError(e: Throwable) {
                                    showToast("error" + (e.message ?: "null"))
                                }

                            })
                    }else{
                        Toast.makeText(this, "write your score", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "write your course", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "write your last name", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "write your first name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }
}