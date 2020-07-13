package com.illidant.easykanzicapstone.platform.api
import com.illidant.easykanzicapstone.domain.model.Kanji
import com.illidant.easykanzicapstone.domain.model.Lesson
import com.illidant.easykanzicapstone.domain.model.Level
import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.model.User
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(ApiConstant.URL_LOGIN)
    fun signin(@Body body: SigninRequest): Call<User>
    @POST(ApiConstant.URL_REGISTER)
    fun signup(@Body body: SignupRequest): Call<SignupResponse>
    @GET(ApiConstant.URL_LEVELS)
    fun getLevelData(): Call<List<Level>>
    @GET(ApiConstant.URL_KANJI)
    fun kanjiLessonRequest(@Path("id")id: Int): Call<List<Kanji>>
    @GET(ApiConstant.URL_LESSON_BY_LEVEL)
    fun getLessonByLevelID(@Path("id")id: Int): Call<List<Lesson>>

}
