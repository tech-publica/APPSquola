package com.example.appsquola

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private const val URL = "http://192.168.1.50:8080/api/"
    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    // Create OkHttp Client
    private val okHttp = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(logger)
        .build()

    // Create Retrofit Builder
    private val retrofit = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp)
        .build()

    // Create Retrofit Instance
    //private val retrofit = builder

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }
}