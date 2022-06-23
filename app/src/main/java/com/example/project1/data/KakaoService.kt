package com.example.project1.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoService {

    @GET("/v3/search/book")
    suspend fun getBlogs(
        @Header("Authorization") key: String,
        @Query("query") queryString: String,
        @Query("sort") sort: String,
    ) : Response<ResponseWrapper>

}