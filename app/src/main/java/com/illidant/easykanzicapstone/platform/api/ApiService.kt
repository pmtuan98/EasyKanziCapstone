package com.illidant.easykanzicapstone.platform.api
import com.illidant.easykanzicapstone.domain.model.*
import com.illidant.easykanzicapstone.domain.request.ResetPasswordRequest
import com.illidant.easykanzicapstone.domain.response.SignupResponse
import com.illidant.easykanzicapstone.domain.request.SigninRequest
import com.illidant.easykanzicapstone.domain.request.SignupRequest
import com.illidant.easykanzicapstone.domain.response.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(ApiConstant.URL_LOGIN)
    fun signin(@Body body: SigninRequest): Call<User>

    @POST(ApiConstant.URL_REGISTER)
    fun signup(@Body body: SignupRequest): Call<SignupResponse>

    @POST(ApiConstant.URL_RESET_PASSWORD)
    fun resetPass(@Body body: ResetPasswordRequest): Call<ResetPasswordResponse>

    @GET(ApiConstant.URL_GET_ALL_LEVELS)
    fun getLevelData(): Call<List<Level>>

    @GET(ApiConstant.URL_GET_KANJI_BY_LESSON_ID)
    fun getKanjiByLessonID(@Path("id")id: Int): Call<List<Kanji>>

    @GET(ApiConstant.URL_GET_LESSON_BY_LEVEL_ID)
    fun getLessonByLevelID(@Path("id")id: Int): Call<List<Lesson>>

    @GET(ApiConstant.URL_GET_KANJI_BY_ID)
    fun getKanjiByID(@Path("id")id: Int): Call<Kanji>

    @GET(ApiConstant.URL_GET_VOCAB_BY_KANJI_ID)
    fun getVocabByKanjiID(@Path("id")id: Int): Call<List<Vocabulary>>

    @GET(ApiConstant.URL_GET_VOCAB_BY_LESSON_ID)
    fun getVocabByLessonID(@Path("id")id: Int): Call<List<Vocabulary>>

    @GET(ApiConstant.URL_GET_QUIZ_BY_LESSON_ID)
    fun getQuizByLessonID(@Path("id")id: Int): Call<List<Quiz>>
}
