package com.example.movierama.data.network

import com.google.gson.annotations.SerializedName

open class BasePageResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)
