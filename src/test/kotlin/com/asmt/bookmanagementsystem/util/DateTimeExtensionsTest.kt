package com.asmt.bookmanagementsystem.util

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import com.asmt.bookmanagementsystem.util.DateTimeExtensions.toIsoString
import com.asmt.bookmanagementsystem.util.DateTimeExtensions.toCustomString

class DateTimeExtensionsTest {

    @Test
    fun `should generate ISO timestamp`() {
        // When
        val timestamp = DateTimeExtensions.nowIsoTimestamp()
        
        // Then
        assertNotNull(timestamp)
        assertTrue(timestamp.contains("T"), "Timestamp should contain 'T': $timestamp")
        assertTrue(timestamp.contains("-"), "Timestamp should contain '-': $timestamp")
        assertTrue(timestamp.contains(":"), "Timestamp should contain ':': $timestamp")
    }

    @Test
    fun `should generate custom timestamp`() {
        // When
        val timestamp = DateTimeExtensions.nowCustomTimestamp()
        
        // Then
        assertNotNull(timestamp)
        assertTrue(timestamp.contains("T"), "Timestamp should contain 'T': $timestamp")
        assertTrue(timestamp.contains("-"), "Timestamp should contain '-': $timestamp")
        assertTrue(timestamp.contains(":"), "Timestamp should contain ':': $timestamp")
    }

    @Test
    fun `should format LocalDateTime to ISO string using extension`() {
        // Given
        val dateTime = LocalDateTime.of(2023, 12, 25, 10, 30, 45)
        
        // When
        val result = dateTime.toIsoString()
        
        // Then
        assertEquals("2023-12-25T10:30:45", result)
    }

    @Test
    fun `should format LocalDateTime to custom string using extension`() {
        // Given
        val dateTime = LocalDateTime.of(2023, 12, 25, 10, 30, 45)
        
        // When
        val result = dateTime.toCustomString()
        
        // Then
        assertEquals("2023-12-25T10:30:45", result)
    }

    @Test
    fun `should format LocalDateTime with custom format using extension`() {
        // Given
        val dateTime = LocalDateTime.of(2023, 12, 25, 10, 30, 45)
        val customFormat = "yyyy/MM/dd HH:mm:ss"
        
        // When
        val result = dateTime.toCustomString(customFormat)
        
        // Then
        assertEquals("2023/12/25 10:30:45", result)
    }

    @Test
    fun `should handle leap year correctly`() {
        // Given - Leap year 2024
        val leapYearDate = LocalDateTime.of(2024, 2, 29, 15, 30, 45)
        
        // When
        val result = leapYearDate.toIsoString()
        
        // Then
        assertEquals("2024-02-29T15:30:45", result)
    }

    @Test
    fun `should handle non-leap year correctly`() {
        // Given - Non-leap year 2023 (February 28 only)
        val nonLeapYearDate = LocalDateTime.of(2023, 2, 28, 15, 30, 45)
        
        // When
        val result = nonLeapYearDate.toIsoString()
        
        // Then
        assertEquals("2023-02-28T15:30:45", result)
    }

    @Test
    fun `should handle century leap year correctly`() {
        // Given - Century leap year 2000 (divisible by 400)
        val centuryLeapYear = LocalDateTime.of(2000, 2, 29, 12, 0, 0)
        
        // When
        val result = centuryLeapYear.toIsoString()
        
        // Then
        assertEquals("2000-02-29T12:00:00", result)
    }

    @Test
    fun `should handle non-century leap year correctly`() {
        // Given - Non-century leap year 2100 (divisible by 100 but not 400)
        val nonCenturyLeapYear = LocalDateTime.of(2100, 2, 28, 12, 0, 0)
        
        // When
        val result = nonCenturyLeapYear.toIsoString()
        
        // Then
        assertEquals("2100-02-28T12:00:00", result)
    }

    @Test
    fun `should handle leap year with custom format`() {
        // Given - Leap year with custom format
        val leapYearDate = LocalDateTime.of(2024, 2, 29, 15, 30, 45)
        val customFormat = "yyyy-MM-dd HH:mm:ss"
        
        // When
        val result = leapYearDate.toCustomString(customFormat)
        
        // Then
        assertEquals("2024-02-29 15:30:45", result)
    }

    @Test
    fun `should generate different timestamps`() {
        // When
        val timestamp1 = DateTimeExtensions.nowIsoTimestamp()
        Thread.sleep(100) // Small delay
        val timestamp2 = DateTimeExtensions.nowIsoTimestamp()
        
        // Then
        assertNotNull(timestamp1)
        assertNotNull(timestamp2)
        assertTrue(timestamp1 != timestamp2, "Timestamps should be different: $timestamp1 vs $timestamp2")
    }
}
