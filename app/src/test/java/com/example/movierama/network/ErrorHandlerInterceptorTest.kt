package com.example.movierama.network

import com.example.movierama.network.interceptors.AuthFailedException
import com.example.movierama.network.interceptors.DeserializationException
import com.example.movierama.network.interceptors.ErrorHandlerInterceptor
import com.example.movierama.network.interceptors.NoInternetException
import com.example.movierama.network.interceptors.ServerException
import com.example.movierama.network.interceptors.ServerNotFoundException
import com.google.common.truth.Truth.assertThat
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ErrorHandlerInterceptorTest {

    @Mock
    private lateinit var chain: Interceptor.Chain

    @Mock
    private lateinit var request: Request

    @Mock
    private lateinit var response: Response

    private val connectionController = FakeConnectionController()


    private lateinit var errorHandlerInterceptor: ErrorHandlerInterceptor

    @Before
    fun setup() {
        request = mock()
        response = mock()
        chain = mock()
        `when`(chain.request()).thenReturn(request)
        `when`(chain.proceed(request)).thenReturn(response)
        errorHandlerInterceptor = ErrorHandlerInterceptor(connectionController)
    }

    @Test
    fun `test no internet connection`() {
        // Given
        connectionController.fakeConnection = false

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: NoInternetException) {
            // Then
            assertThat(e.message).isEqualTo("No internet connection")
        }
    }

    @Test
    fun `test auth failed 401`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(401)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: AuthFailedException) {
            // Then
            assertThat(e.message).isEqualTo("Authorization failed")
        }
    }

    @Test
    fun `test auth failed 403`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(403)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: AuthFailedException) {
            // Then
            assertThat(e.message).isEqualTo("Authorization failed")
        }
    }

    @Test
    fun `test server error 501`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(501)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: ServerException) {
            // Then
            assertThat(e.message).isEqualTo("Server failed")
        }
    }

    @Test
    fun `test server error 500`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(500)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: ServerException) {
            // Then
            assertThat(e.message).isEqualTo("Server failed")
        }
    }

    @Test
    fun `test server not found error 404`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(404)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: ServerNotFoundException) {
            // Then
            assertThat(e.message).isEqualTo("Server not found")
        }
    }

    @Test
    fun `test response not successful`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.isSuccessful).thenReturn(false)

        // When
        try {
            errorHandlerInterceptor.intercept(chain)
        } catch (e: DeserializationException) {
            // Then
            assertThat(e.message).isEqualTo("Error parsing response")
        }
    }

    @Test
    fun `test response is successful 200`() {
        // Given
        connectionController.fakeConnection = true
        `when`(response.code).thenReturn(200)
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.message).thenReturn("success message")


        try {
            // When
            val result = errorHandlerInterceptor.intercept(chain)

            // Then
            assertThat(result.message).isEqualTo("success message")
        } catch (_: Exception) {
        }
    }

}