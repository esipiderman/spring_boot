package com.example.springboot.utils

import com.example.springboot.model.api.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceSingleton {

    var apiService : ApiService? = null
    get() {
        if (field==null){
            val retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            field = retrofit.create(ApiService::class.java)
        }
        return field
    }
}