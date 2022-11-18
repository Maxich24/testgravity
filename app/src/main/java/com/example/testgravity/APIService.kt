package com.example.testgravity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("prod")
    fun getData(): Call<String?>?
}