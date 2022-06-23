package com.example.project1.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoServiceImpl {
    private const val BASE_URL = "https://dapi.kakao.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val kakaoService = retrofit.create(KakaoService::class.java)
}

