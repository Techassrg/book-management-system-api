package com.asmt.Book_Management_System.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check API", description = "API for checking application health status")
class HealthController {

    @GetMapping
    @Operation(
        summary = "Check application health",
        description = "Returns the current health status of the application"
    )
    fun getHealth(): ResponseEntity<Map<String, Any>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = LocalDateTime.now().format(formatter)
        
        val response = mapOf(
            "status" to "UP",
            "timestamp" to currentTime,
            "message" to "Book Management System is running",
            "version" to "1.0.0",
                               "database" to "MySQL"
        )
        
        return ResponseEntity.ok(response)
    }

    @GetMapping("/simple")
    @Operation(
        summary = "Simple health check",
        description = "Returns a simple health status without detailed information"
    )
    fun getSimpleHealth(): ResponseEntity<Map<String, String>> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = LocalDateTime.now().format(formatter)
        
        val response = mapOf(
            "status" to "UP",
            "timestamp" to currentTime,
            "message" to "Book Management System is running"
        )
        
        return ResponseEntity.ok(response)
    }
} 