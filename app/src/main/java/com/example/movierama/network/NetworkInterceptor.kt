package com.example.movierama.network

import com.example.movierama.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor, RequestHeader {

    companion object {
        const val HEADER_AUTH = "Authorization"
        const val ContentType = "Content-Type"
        const val ACCEPTANCE = "accept"
        const val AUTH_TYPE = "Bearer"
    }

    private val tag = NetworkInterceptor::class.java.simpleName

    private fun getRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader(HEADER_AUTH, getBasicAuthentication())
            .addHeader(ContentType, "application/json")
            .addHeader(ACCEPTANCE, "application/json")
            .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(getRequest(chain.request()))
    }

    override fun getBasicAuthentication(): String = AUTH_TYPE + " " + BuildConfig.BASE_API_KEY

}