package com.example.movierama.domain.error_handling


import com.example.movierama.R
import com.example.movierama.core.domain.errorhadling.ErrorHandler
import com.example.movierama.core.domain.errorhadling.getErrorMessageResource
import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ErrorHandlerTest {

    private lateinit var errorHandler: ErrorHandler

    @Test
    fun `test get exception error string message with an IOException`() {
        // Given
        val exception = IOException("This is a test exception")
        errorHandler = FakeErrorHandler().apply { exceptionErrorMessage = "Failed to read the response" }
        val expectedResult = "Failed to read the response"

        // When
        val result = ""

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get exception resource message with IOException`() {
        // Given
        val exception = IOException("This is a test exception")
        val expectedResult = R.string.error_reading_response

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get exception resource message with general Exception`() {
        // Given
        val exception = Exception("This is a test exception")
        val expectedResult = R.string.error_generic

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get exception resource message with general HttpException`() {
        // Given
        val response = Response.error<Any>(
            404,
            "".toResponseBody("application/json".toMediaTypeOrNull())
        )
        val exception = HttpException(response)
        val expectedResult = R.string.error_network

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get throwable resource message with IOException`() {
        // Given
        val exception = IOException("This is a test exception")
        val expectedResult = R.string.error_reading_response

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get throwable resource message with general Exception`() {
        // Given
        val exception = Exception("This is a test exception")
        val expectedResult = R.string.error_generic

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `test get throwable resource message with general HttpException`() {
        // Given
        val response = Response.error<Any>(
            404,
            "".toResponseBody("application/json".toMediaTypeOrNull())
        )
        val exception = HttpException(response)
        val expectedResult = R.string.error_network

        // When
        val result = exception.getErrorMessageResource()

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

}
