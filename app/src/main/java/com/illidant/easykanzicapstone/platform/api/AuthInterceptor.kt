package com.illidant.easykanzicapstone.platform.api

import android.content.Context
import com.illidant.easykanzicapstone.platform.source.local.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

const val HEADER_ACCESS_TOKEN = "access-token"
const val CONTENT_TYPE = "Content-Type"

class AccessTokenInterceptor(context: Context) : Interceptor {

    private val prefs = SharedPrefs(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token: String
        prefs.token?.let { token = it }
        val newRequest = prefs.token?.let {
            request.newBuilder()
                .addHeader(CONTENT_TYPE, "application/json")
                .addHeader(HEADER_ACCESS_TOKEN, it)
                .build()
        }
        return chain.proceed(newRequest)
    }
}
