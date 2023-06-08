package com.example.movierama.network

import com.example.movierama.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor, RequestHeader {

    private val tag = NetworkInterceptor::class.java.simpleName

    private fun getRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader("Authorization", getBasicAuthentication())
            .addHeader("Content-Type", "application/json")
            .addHeader("accept", "application/json")
            .build()
    }

    override fun getBasicAuthentication(): String = "Bearer " + BuildConfig.BASE_API_KEY

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val initialRequest = chain.request()

        // Assemble new request
        val request = getRequest(initialRequest)

        // Make the request and get the response
        val response = chain.proceed(request)

        // Get content type and body. At this point the response is consumed, so it must be built again on return
        val contentType = response.body?.contentType()
        val bodyString = response.body?.string() ?: throw IOException("Empty response body")

        // Rebuild and return the response
        val body = ResponseBody.create(contentType, bodyString)
        return response.newBuilder().body(body).build()
    }

}