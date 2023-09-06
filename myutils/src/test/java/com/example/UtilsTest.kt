package com.example

import com.example.myutils.isNumber
import com.example.myutils.roundToTwoDecimal
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {

    @Test
    fun `test isNumber check function`() {
        // Given
        val numberInput = "24"
        val stringInput = "abc"

        // Then
        assertThat(numberInput.isNumber()).isTrue()
        assertThat(stringInput.isNumber()).isFalse()
    }

    @Test
    fun `test round to 2 decimals function`() {
        // Given
        val float = 2.463593F

        // When
        val result = float.roundToTwoDecimal()

        // Then
        assertThat(result).isEqualTo(2.46F)
    }

    @Test
    fun `test round to 2 decimals function with not decimals`() {
        // Given
        val float = 2F

        // When
        val result = float.roundToTwoDecimal()

        // Then
        assertThat(result).isEqualTo(2)
    }

}
