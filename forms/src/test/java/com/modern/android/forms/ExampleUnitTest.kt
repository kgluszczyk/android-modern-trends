package com.modern.android.forms

import assertk.assertThat
import assertk.assertions.isTrue
import com.modern.android.forms.entity.Input
import com.modern.android.validateMandatoryFields
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import org.junit.Assert.*
import kotlin.random.Random

class ExampleUnitTest {

    @Test
    fun `no errors while value not empty`() {
        //given
        val inputs = (1..10).map {
            mockk<Input> {
                every { id } returns it.toLong()
                every { value } returns (Random.nextInt(50) + 1).getRandomString()
                every { mandatory } returns true
                every { validation } returns null
            }
        }
        //when
        val errors = inputs.validateMandatoryFields()

        //then
        assertThat(errors.isEmpty()).isTrue()
    }
}

fun Int.getRandomString(): String {
    val allowedChars = ('A'..'Z') + ('a'..'z')
    return (1..this)
        .map { allowedChars.random() }
        .joinToString("")
}