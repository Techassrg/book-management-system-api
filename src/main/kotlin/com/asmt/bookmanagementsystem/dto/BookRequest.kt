package com.asmt.bookmanagementsystem.dto

import com.asmt.bookmanagementsystem.entity.BookStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Min
import java.time.LocalDate
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class BookRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String,

    @field:NotBlank(message = "Author is required")
    @field:Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    val author: String,

    @field:NotBlank(message = "Published date is required")
    @field:Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "Published date must be in format yyyy-MM-dd (Buddhist calendar)"
    )
    val publishedDate: String, // yyyy-MM-dd in Buddhist calendar
    
    val status: BookStatus = BookStatus.AVAILABLE
) {
    fun toGregorianDate(): LocalDate {
        return try {
            // Parse the Buddhist date components
            val parts = publishedDate.split("-")
            if (parts.size != 3) {
                throw IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd")
            }
            
            // Validate that all parts are numeric and have correct format
            if (!parts[0].matches(Regex("\\d{4}")) || 
                !parts[1].matches(Regex("\\d{2}")) || 
                !parts[2].matches(Regex("\\d{2}"))) {
                throw IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd")
            }
            
            val buddhistYear = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            
            // Validate month and day ranges
            if (month < 1 || month > 12) {
                throw IllegalArgumentException("Invalid month. Month must be between 1 and 12.")
            }
            if (day < 1 || day > 31) {
                throw IllegalArgumentException("Invalid day. Day must be between 1 and 31.")
            }
            
            // Convert Buddhist year to Gregorian year
            val gregorianYear = buddhistYear - 543
            
            // Create the Gregorian date
            LocalDate.of(gregorianYear, month, day)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd")
        } catch (e: DateTimeException) {
            throw IllegalArgumentException("Invalid date. The date does not exist in the Gregorian calendar.")
        }
    }
}
