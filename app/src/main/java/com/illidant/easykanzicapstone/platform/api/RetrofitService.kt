package com.illidant.easykanzicapstone.platform.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitService private constructor(private val context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AccessTokenInterceptor(context))
        .readTimeout(ApiConstant.READ_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(ApiConstant.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun getService(): ApiService = retrofit.create(ApiService::class.java)

    companion object {
        private var INSTANCE: RetrofitService? = null

        fun getInstance(context: Context): RetrofitService {
            return INSTANCE ?: synchronized(this) {
                val instance = RetrofitService(context)
                INSTANCE = instance
                instance
            }
        }
    }
}
