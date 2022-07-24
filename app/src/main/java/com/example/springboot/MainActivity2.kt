package com.example.springboot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.springboot.apiManager.ApiService
import com.example.springboot.databinding.ActivityMain2Binding
import com.example.springboot.recyclerMain.Student
import com.google.gson.JsonObject
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    lateinit var apiService: ApiService
    lateinit var disposable:Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAddStudent.toolbar)

        binding.toolbarAddStudent.toolbar.title = "Add New Student"

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.edtFirstName.requestFocus()

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        var isUpdating = intent.getParcelableExtra<Student>("updateStudent") != null

        if (isUpdating){
            val dataFromIntent = intent.getParcelableExtra<Student>("updateStudent")!!
            binding.edtCourse.setText(dataFromIntent.course)
            binding.edtScore.setText(dataFromIntent.score.toString())

            val splitedName = dataFromIntent.name.split(" ")
            binding.edtFirstName.setText(splitedName[0])
            binding.edtLastName.setText(splitedName[1])
        }

        binding.btnDone.setOnClickListener {
            if (isUpdating){
                updateStudent()
            }else{
                insertStudent()
            }
        }
    }

    private fun updateStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (firstName.isNotEmpty()){
            if (lastName.isNotEmpty()){
                val jsonObject = JsonObject()
                jsonObject.addProperty("name", "$firstName $lastName")

                if (course.isNotEmpty()){
                    jsonObject.addProperty("course", course)

                    if (score.isNotEmpty()){
                        jsonObject.addProperty("score", score.toInt())

                        apiService.updateStudent("$firstName $lastName", jsonObject).enqueue(object :Callback<String>{
                            override fun onResponse(call: Call<String>, response: Response<String>) {}

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                Log.v("apiLog", t.message!!)
                            }

                        })
                        onBackPressed()
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

    private fun insertStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val course = binding.edtCourse.text.toString()
        val score = binding.edtScore.text.toString()

        if (firstName.isNotEmpty()){
            if (lastName.isNotEmpty()){
                val jsonObject = JsonObject()
                jsonObject.addProperty("name", "$firstName $lastName")

                if (course.isNotEmpty()){
                    jsonObject.addProperty("course", course)

                    if (score.isNotEmpty()){
                        jsonObject.addProperty("score", score.toInt())

                        apiService
                            .insertStudent(jsonObject)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object :SingleObserver<String>{
                                override fun onSubscribe(d: Disposable) {
                                    disposable = d
                                }

                                override fun onSuccess(t: String) {}

                                override fun onError(e: Throwable) {
                                    Log.v("testRxJava", e.message!!)
                                }

                            })
                        onBackPressed()
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