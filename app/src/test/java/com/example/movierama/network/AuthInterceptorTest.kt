package com.example.movierama.network

import com.example.movierama.BuildConfig
import com.example.movierama.network.domain.interceptors.AuthInterceptor
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AuthInterceptorTest {

    @Mock
    private lateinit var chain: Interceptor.Chain

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var requestBuilder: Request.Builder

    @Mock
    private lateinit var response: Response

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testIntercept() {
        val headers = Headers.Builder()
            .add(AuthInterceptor.HEADER_AUTH, "Bearer " + BuildConfig.BASE_API_KEY)
            .add(AuthInterceptor.ContentType, "application/json")
            .add(AuthInterceptor.ACCEPTANCE, "application/json")
            .build()

        `when`(request.newBuilder()).thenReturn(requestBuilder)
        `when`(
            requestBuilder.addHeader(
                AuthInterceptor.HEADER_AUTH,
                "Bearer " + BuildConfig.BASE_API_KEY
            )
        ).thenReturn(requestBuilder)
        `when`(
            requestBuilder.addHeader(
                AuthInterceptor.ContentType,
                "application/json"
            )
        ).thenReturn(requestBuilder)
        `when`(requestBuilder.addHeader(AuthInterceptor.ACCEPTANCE, "application/json")).thenReturn(
            requestBuilder
        )
        `when`(requestBuilder.build()).thenReturn(request)
        `when`(chain.request()).thenReturn(request)
        `when`(chain.proceed(request)).thenReturn(response)
        `when`(response.request).thenReturn(request)
        `when`(response.request.headers).thenReturn(headers)

        val interceptor = AuthInterceptor()
        val interceptedResponse = interceptor.intercept(chain)

        assertEquals(request, interceptedResponse.request)

        val interceptedHeaders = interceptedResponse.request.headers

        assertEquals(3, interceptedHeaders.size)
        assertEquals(
            "Bearer " + BuildConfig.BASE_API_KEY,
            interceptedHeaders[AuthInterceptor.HEADER_AUTH]
        )
        assertEquals("application/json", interceptedHeaders[AuthInterceptor.ContentType])
        assertEquals("application/json", interceptedHeaders[AuthInterceptor.ACCEPTANCE])
    }

    @Test
    fun testGetBasicAuthentication() {
        val interceptor = AuthInterceptor()
        val expectedAuth = "Bearer " + BuildConfig.BASE_API_KEY
        val auth = interceptor.getBasicAuthentication()
        assertEquals(expectedAuth, auth)
    }
}
