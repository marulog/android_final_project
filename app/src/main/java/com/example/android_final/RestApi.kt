package com.example.android_final

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface RestApi {
    @GET("150") // (a) 응답
    fun getImage(): Call<ResponseBody> // (b) 응답
}
