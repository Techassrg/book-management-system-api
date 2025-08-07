package com.asmt.bookmanagementsystem.dto

import com.asmt.bookmanagementsystem.entity.BookStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.DateTimeException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import java.time.format.DateTimeParseException
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

class BookRequestTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun `should convert Buddhist calendar to Gregorian calendar correctly`() {
        // Given - Buddhist calendar dates
        val testCases = mapOf(
            "2568-01-01" to LocalDate.of(2025, 1, 1),  // Buddhist 2568 = Gregorian 2025
            "2567-12-31" to LocalDate.of(2024, 12, 31), // Buddhist 2567 = Gregorian 2024
            "2500-06-15" to LocalDate.of(1957, 6, 15),  // Buddhist 2500 = Gregorian 1957
            "2600-08-20" to LocalDate.of(2057, 8, 20)   // Buddhist 2600 = Gregorian 2057
        )

        testCases.forEach { (buddhistDate, expectedGregorian) ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )
            val result = bookRequest.toGregorianDate()

            // Then
            assertEquals(expectedGregorian, result, "Failed for Buddhist date: $buddhistDate")
        }
    }

    @Test
    fun `should handle leap year in Buddhist calendar correctly`() {
        // Given - Buddhist leap year dates (only valid ones)
        val leapYearCases = mapOf(
            "2567-02-29" to LocalDate.of(2024, 2, 29), // Buddhist 2567 = Gregorian 2024 (leap year)
            "2543-02-29" to LocalDate.of(2000, 2, 29)  // Buddhist 2543 = Gregorian 2000 (century leap year)
        )

        leapYearCases.forEach { (buddhistDate, expectedGregorian) ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )

            // Then
            val result = bookRequest.toGregorianDate()
            assertEquals(expectedGregorian, result, "Failed for Buddhist leap year: $buddhistDate")
        }
    }

    @Test
    fun `should handle century leap year in Buddhist calendar correctly`() {
        // Given - Century leap year cases
        val validLeapYear = "2543-02-29" // Buddhist 2543 = Gregorian 2000 (century leap year)
        val invalidLeapYears = listOf(
            "2544-02-29", // Buddhist 2544 = Gregorian 2001 (not leap year - should fail)
            "2548-02-29"  // Buddhist 2548 = Gregorian 2005 (not leap year - should fail)
        )

        // Test valid century leap year
        val bookRequest = BookRequest(
            title = "Test Book",
            author = "Test Author",
            publishedDate = validLeapYear
        )
        val result = bookRequest.toGregorianDate()
        assertEquals(LocalDate.of(2000, 2, 29), result, "Failed for Buddhist century leap year: $validLeapYear")

        // Test invalid leap years
        invalidLeapYears.forEach { buddhistDate ->
            val invalidBookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )
            assertThrows<IllegalArgumentException> {
                invalidBookRequest.toGregorianDate()
            }
        }
    }

    @Test
    fun `should handle non-leap year February correctly`() {
        // Given - Non-leap year February dates
        val nonLeapYearCases = mapOf(
            "2568-02-28" to LocalDate.of(2025, 2, 28), // Buddhist 2568 = Gregorian 2025 (non-leap year)
            "2569-02-28" to LocalDate.of(2026, 2, 28), // Buddhist 2569 = Gregorian 2026 (non-leap year)
            "2570-02-28" to LocalDate.of(2027, 2, 28)  // Buddhist 2570 = Gregorian 2027 (non-leap year)
        )

        nonLeapYearCases.forEach { (buddhistDate, expectedGregorian) ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )
            val result = bookRequest.toGregorianDate()

            // Then
            assertEquals(expectedGregorian, result, "Failed for Buddhist non-leap year: $buddhistDate")
        }
    }

    @Test
    fun `should throw exception for invalid Buddhist leap year dates`() {
        // Given - Invalid leap year dates
        val invalidLeapYearDates = listOf(
            "2568-02-29", // Buddhist 2568 = Gregorian 2025 (not leap year)
            "2569-02-29", // Buddhist 2569 = Gregorian 2026 (not leap year)
            "2570-02-29", // Buddhist 2570 = Gregorian 2027 (not leap year)
            "2643-02-29"  // Buddhist 2643 = Gregorian 2100 (not century leap year)
        )

        invalidLeapYearDates.forEach { buddhistDate ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )

            // Then - These should throw IllegalArgumentException when trying to create LocalDate
            assertThrows<IllegalArgumentException> {
                bookRequest.toGregorianDate()
            }
        }
    }

    @Test
    fun `should handle edge cases correctly`() {
        // Given - Edge cases
        val edgeCases = mapOf(
            "2567-01-01" to LocalDate.of(2024, 1, 1),  // Start of year
            "2567-12-31" to LocalDate.of(2024, 12, 31), // End of year
            "2500-01-01" to LocalDate.of(1957, 1, 1),   // Early year
            "2600-12-31" to LocalDate.of(2057, 12, 31)  // Future year
        )

        edgeCases.forEach { (buddhistDate, expectedGregorian) ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = buddhistDate
            )
            val result = bookRequest.toGregorianDate()

            // Then
            assertEquals(expectedGregorian, result, "Failed for edge case: $buddhistDate")
        }
    }

    @Test
    fun `should create BookRequest with default status`() {
        // Given
        val title = "Test Book"
        val author = "Test Author"
        val publishedDate = "2567-01-01"

        // When
        val bookRequest = BookRequest(
            title = title,
            author = author,
            publishedDate = publishedDate
        )

        // Then
        assertEquals(title, bookRequest.title)
        assertEquals(author, bookRequest.author)
        assertEquals(publishedDate, bookRequest.publishedDate)
        assertEquals(BookStatus.AVAILABLE, bookRequest.status)
    }

    @Test
    fun `should create BookRequest with custom status`() {
        // Given
        val title = "Test Book"
        val author = "Test Author"
        val publishedDate = "2567-01-01"
        val status = BookStatus.BORROWED

        // When
        val bookRequest = BookRequest(
            title = title,
            author = author,
            publishedDate = publishedDate,
            status = status
        )

        // Then
        assertEquals(title, bookRequest.title)
        assertEquals(author, bookRequest.author)
        assertEquals(publishedDate, bookRequest.publishedDate)
        assertEquals(status, bookRequest.status)
    }

    @Test
    fun `should throw exception for invalid date format`() {
        // Given - Invalid date formats
        val invalidDates = listOf(
            "2567/01/01",  // Wrong separator
            "2567.01.01",  // Wrong separator
            "2567-1-1",    // Missing leading zeros
            "2567-13-01",  // Invalid month
            "2567-01-32",  // Invalid day
            "2567-00-01",  // Invalid month
            "2567-01-00",  // Invalid day
            "invalid-date", // Completely invalid
            ""             // Empty string
        )

        invalidDates.forEach { invalidDate ->
            // When
            val bookRequest = BookRequest(
                title = "Test Book",
                author = "Test Author",
                publishedDate = invalidDate
            )

            // Then
            assertThrows<IllegalArgumentException> {
                bookRequest.toGregorianDate()
            }
        }
    }

    @Test
    fun `should validate title is not blank`() {
        val bookRequest = BookRequest(
            title = "",
            author = "Test Author",
            publishedDate = "2567-01-01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(2, violations.size) // Both @NotBlank and @Size violations
        val messages = violations.map { it.message }
        assert(messages.contains("Title is required"))
        assert(messages.contains("Title must be between 1 and 255 characters"))
    }

    @Test
    fun `should validate title is not null`() {
        // This test is not needed since Kotlin data class requires non-null parameters
        // The compiler will prevent null values at compile time
    }

    @Test
    fun `should validate title length`() {
        val longTitle = "a".repeat(256)
        val bookRequest = BookRequest(
            title = longTitle,
            author = "Test Author",
            publishedDate = "2567-01-01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(1, violations.size)
        assertEquals("Title must be between 1 and 255 characters", violations.first().message)
    }

    @Test
    fun `should validate author is not blank`() {
        val bookRequest = BookRequest(
            title = "Test Book",
            author = "",
            publishedDate = "2567-01-01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(2, violations.size) // Both @NotBlank and @Size violations
        val messages = violations.map { it.message }
        assert(messages.contains("Author is required"))
        assert(messages.contains("Author must be between 1 and 255 characters"))
    }

    @Test
    fun `should validate author is not null`() {
        // This test is not needed since Kotlin data class requires non-null parameters
        // The compiler will prevent null values at compile time
    }

    @Test
    fun `should validate author length`() {
        val longAuthor = "a".repeat(256)
        val bookRequest = BookRequest(
            title = "Test Book",
            author = longAuthor,
            publishedDate = "2567-01-01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(1, violations.size)
        assertEquals("Author must be between 1 and 255 characters", violations.first().message)
    }

    @Test
    fun `should validate publishedDate is not blank`() {
        val bookRequest = BookRequest(
            title = "Test Book",
            author = "Test Author",
            publishedDate = ""
        )

        val violations = validator.validate(bookRequest)
        assertEquals(2, violations.size) // Both @NotBlank and @Pattern violations
        val messages = violations.map { it.message }
        assert(messages.contains("Published date is required"))
        assert(messages.contains("Published date must be in format yyyy-MM-dd (Buddhist calendar)"))
    }

    @Test
    fun `should validate publishedDate is not null`() {
        // This test is not needed since Kotlin data class requires non-null parameters
        // The compiler will prevent null values at compile time
    }

    @Test
    fun `should validate publishedDate format`() {
        val bookRequest = BookRequest(
            title = "Test Book",
            author = "Test Author",
            publishedDate = "2567/01/01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(1, violations.size)
        assertEquals("Published date must be in format yyyy-MM-dd (Buddhist calendar)", violations.first().message)
    }

    @Test
    fun `should validate multiple violations`() {
        val bookRequest = BookRequest(
            title = "",
            author = "",
            publishedDate = "invalid"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(5, violations.size) // 2 for title + 2 for author + 1 for publishedDate
        
        val messages = violations.map { it.message }.toSet()
        assert(messages.contains("Title is required"))
        assert(messages.contains("Title must be between 1 and 255 characters"))
        assert(messages.contains("Author is required"))
        assert(messages.contains("Author must be between 1 and 255 characters"))
        assert(messages.contains("Published date must be in format yyyy-MM-dd (Buddhist calendar)"))
    }

    @Test
    fun `should pass validation for valid data`() {
        val bookRequest = BookRequest(
            title = "Valid Book",
            author = "Valid Author",
            publishedDate = "2567-01-01"
        )

        val violations = validator.validate(bookRequest)
        assertEquals(0, violations.size)
    }
}
