package com.example

import com.example.myutils.isNumber
import com.google.common.truth.Truth
import org.junit.Test

class UtilsTest {

    @Test
    fun `test isNumber check function`() {
        // Given
        val numberInput = "24"
        val stringInput = "abc"

        // Then
        Truth.assertThat(numberInput.isNumber()).isTrue()
        Truth.assertThat(stringInput.isNumber()).isFalse()
    }
}
