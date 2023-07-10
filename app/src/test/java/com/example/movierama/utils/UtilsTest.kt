package com.example.movierama.utils

import com.example.movierama.ui.utils.isNumber
import com.example.movierama.ui.utils.mapToDate
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun `test mapToDate function`() {
        // Given
        val givenDate = "2000-03-25"

        // When
        val expectedResult = "25 March 00"

        // Then
        assertThat(expectedResult).isEqualTo(givenDate.mapToDate())
    }

    @Test
    fun `test isNumber check function`() {
        // Given
        val numberInput = "24"
        val stringInput = "abc"

        // Then
        assertThat(numberInput.isNumber()).isTrue()
        assertThat(stringInput.isNumber()).isFalse()
    }
}
