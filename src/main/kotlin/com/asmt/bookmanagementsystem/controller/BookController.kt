package com.asmt.bookmanagementsystem.controller

import com.asmt.bookmanagementsystem.dto.BookRequest
import com.asmt.bookmanagementsystem.entity.Book
import com.asmt.bookmanagementsystem.entity.BookStatus
import com.asmt.bookmanagementsystem.service.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
@Tag(name = "Book API", description = "API for managing books")
@Validated
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
        @RequestParam @NotBlank(message = "Author name cannot be blank") author: String
    ): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(service.getBooksByAuthor(author))
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get books by status",
        description = "Retrieve all books with the specified status. Uses optimized query with index."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Books found successfully"),
            ApiResponse(responseCode = "400", description = "Invalid status parameter"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getBooksByStatus(
        @Parameter(description = "Book status to filter by", required = true)
        @PathVariable status: BookStatus
    ): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(service.getBooksByStatus(status))
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
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveBook(request))
    }

    @PutMapping("/{id}/status")
    @Operation(
        summary = "Update book status",
        description = "Update the status of a book by its ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Book status updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid status or book ID"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Book not found"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun updateBookStatus(
        @Parameter(description = "Book ID", required = true)
        @PathVariable @Min(value = 1, message = "Book ID must be greater than 0") id: Long,
        @Parameter(description = "New book status", required = true)
        @RequestParam status: BookStatus
    ): ResponseEntity<Book> {
        return ResponseEntity.ok(service.updateBookStatus(id, status))
    }
}
