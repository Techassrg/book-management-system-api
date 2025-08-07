package com.asmt.bookmanagementsystem.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.stream()
            .map { error -> 
                "${error.field}: ${error.defaultMessage}" 
            }
            .collect(Collectors.toList())

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Error",
            message = "Invalid request data",
            details = errors
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid request",
            details = listOf(ex.message ?: "Invalid request")
        )

        return ResponseEntity.badRequest().body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred",
            details = listOf(ex.message ?: "Unknown error")
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val details: List<String>
)
