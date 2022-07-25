package com.example.springboot.utils

import android.content.Context
import android.widget.Toast
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Context.showToast(str:String){
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun<T> Single<T>.asyncRequest() :Single<T>{
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.asyncRequest() :Completable{
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}