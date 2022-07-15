package com.example.springboot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.springboot.apiManager.ApiService
import com.example.springboot.databinding.ActivityMainBinding
import com.example.springboot.recyclerMain.RecyclerAdapter
import com.example.springboot.recyclerMain.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://192.168.1.97:8080"

class MainActivity : AppCompatActivity() , RecyclerAdapter.StudentEvent{
    lateinit var binding: ActivityMainBinding
    lateinit var apiService: ApiService
    lateinit var recyclerAdapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        getDataFromApi()
    }

    private fun getDataFromApi() {

        apiService.getAllStudents().enqueue(object :Callback<List<Student>>{
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                val dataFromServer = response.body()!!
                setDataToRecycler(ArrayList(dataFromServer))
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Log.v("apiLog", t.message!!)
            }

        })

    }

    private fun setDataToRecycler(data:ArrayList<Student>){
        recyclerAdapter = RecyclerAdapter(data, this)
        binding.recyclerMain.adapter = recyclerAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false )
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
    }

    private fun updateDataInServer(student: Student, position: Int) {
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
        apiService.deleteStudent(student.name).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {}
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.v("apiLog", t.message!!)
            }

        })
        recyclerAdapter.removeItem(student, position)
    }
}