package com.asmt.Book_Management_System.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class BookRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,

    @field:NotBlank(message = "Author is required")
    val author: String,

    @field:NotBlank(message = "Published date is required")
    @field:Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "Published date must be in format yyyy-MM-dd (Buddhist calendar)"
    )
    val publishedDate: String // yyyy-MM-dd in Buddhist calendar
) {
    fun toGregorianDate(): LocalDate {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val buddhistDate = LocalDate.parse(publishedDate, formatter)
            buddhistDate.withYear(buddhistDate.year - 543)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd")
        }
    }
}
