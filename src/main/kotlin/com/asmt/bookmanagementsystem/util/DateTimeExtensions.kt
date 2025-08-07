package com.asmt.bookmanagementsystem.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Extension functions for DateTime operations
 */
object DateTimeExtensions {

    /**
     * Get current timestamp in ISO format
     */
    fun nowIsoTimestamp(): String = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    /**
     * Get current timestamp in custom format
     */
    fun nowCustomTimestamp(format: String = "yyyy-MM-dd'T'HH:mm:ss"): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern(format))

    /**
     * Format LocalDateTime to ISO string
     */
    fun LocalDateTime.toIsoString(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    /**
     * Format LocalDateTime to custom string
     */
    fun LocalDateTime.toCustomString(format: String = "yyyy-MM-dd'T'HH:mm:ss"): String =
        this.format(DateTimeFormatter.ofPattern(format))
}
