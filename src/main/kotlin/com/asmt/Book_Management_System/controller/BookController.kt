package com.asmt.Book_Management_System.controller

import com.asmt.Book_Management_System.dto.BookRequest
import com.asmt.Book_Management_System.entity.Book
import com.asmt.Book_Management_System.service.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
@Tag(name = "Book API", description = "API for managing books")
class BookController(private val service: BookService) {

    @GetMapping
    @Operation(
        summary = "Get books by author",
        description = "Retrieve all books written by the specified author. Uses optimized query with index."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Books found successfully"),
            ApiResponse(responseCode = "400", description = "Invalid author parameter"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBooksByAuthor(
        @Parameter(description = "Author name to search for", required = true)
        @RequestParam author: String
    ): ResponseEntity<List<Book>> {
        return try {
            ResponseEntity.ok(service.getBooksByAuthor(author))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping
    @Operation(
        summary = "Create a new book",
        description = "Save a new book to the database. Accepts publishedDate in Buddhist calendar format (yyyy-MM-dd)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Book created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid book data"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun createBook(
        @Parameter(description = "Book data to create", required = true)
        @RequestBody @Valid request: BookRequest
    ): ResponseEntity<Book> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(service.saveBook(request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
