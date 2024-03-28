package com.example.movierama.network.domain.interceptors

import com.example.movierama.BuildConfig
import com.example.movierama.network.domain.RequestHeader
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor, RequestHeader {

    companion object {
        const val HEADER_AUTH = "Authorization"
        const val ContentType = "Content-Type"
        const val ACCEPTANCE = "accept"
        const val AUTH_TYPE = "Bearer"
    }

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
