package com.illidant.easykanzicapstone.platform.api
import com.illidant.easykanzicapstone.domain.model.*
import com.illidant.easykanzicapstone.domain.request.*
import com.illidant.easykanzicapstone.domain.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST(ApiConstant.URL_LOGIN)
    fun signin(@Body body: SigninRequest): Call<User>

    @POST(ApiConstant.URL_REGISTER)
    fun signup(@Body body: SignupRequest): Call<SignupResponse>

    @POST(ApiConstant.URL_RESET_PASSWORD)
    fun resetPass(@Body body: ResetPasswordRequest): Call<ResetPasswordResponse>

    @POST(ApiConstant.URL_CHANGE_PASSWORD)
    fun changePass(@Body body: ChangePasswordRequest): Call<ChangePasswordResponse>

    @POST(ApiConstant.URL_TEST_RESULT)
    fun sendTestResult(@Body body: TestRankingRequest): Call<TestRankingResponse>

    @POST(ApiConstant.URL_SEARCH)
    fun searchKanji(@Body body: SearchRequest): Call<SearchResponse>

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

    @GET(ApiConstant.URL_GET_QUIZ_BY_LEVEL_ID)
    fun getQuizByLevelID(@Path("id")id: Int): Call<List<Quiz>>


}
