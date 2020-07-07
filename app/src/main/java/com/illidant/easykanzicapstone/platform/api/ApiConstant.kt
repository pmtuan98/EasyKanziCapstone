package com.illidant.easykanzicapstone.platform.api

object ApiConstant {
    const val BASE_URL = "http://104.155.187.140:8081/api/"
    const val URL_LOGIN = "auth/signin"
    const val URL_REGISTER = "auth/signup"

    const val HEADER_USERNAME = "username"
    const val HEADER_PASSWORD = "password"

    const val READ_TIMEOUT: Long = 60
    const val CONNECTION_TIMEOUT: Long = 60
}
