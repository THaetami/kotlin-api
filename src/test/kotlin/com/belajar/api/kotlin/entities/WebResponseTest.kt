package com.belajar.api.kotlin.entities

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WebResponseTest {

    @Test
    fun `test WebResponse properties`() {
        // Arrange
        val code = 200
        val status = "Success"
        val data = "Response data"

        // Act
        val webResponse = WebResponse(code, status, data)

        // Assert
        assertEquals(code, webResponse.code)
        assertEquals(status, webResponse.status)
        assertEquals(data, webResponse.data)
    }

}