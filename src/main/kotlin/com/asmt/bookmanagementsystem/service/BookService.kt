package com.asmt.bookmanagementsystem.service

import com.asmt.bookmanagementsystem.dto.BookRequest
import com.asmt.bookmanagementsystem.entity.Book
import com.asmt.bookmanagementsystem.entity.BookStatus
import com.asmt.bookmanagementsystem.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class BookService(private val bookRepository: BookRepository) {

    /**
     * Get books by author with optimized query
     * Uses index on author column for better performance
     */
    fun getBooksByAuthor(author: String): List<Book> {
        if (author.isBlank()) {
            throw IllegalArgumentException("Author name cannot be empty")
        }
        return bookRepository.findByAuthor(author.trim())
    }

    /**
     * Get books by status
     * Uses index on status column for better performance
     */
    fun getBooksByStatus(status: BookStatus): List<Book> {
        return bookRepository.findByStatus(status)
    }

    /**
     * Save a new book with validation
     * Converts Buddhist calendar to Gregorian calendar
     */
    fun saveBook(request: BookRequest): Book {
        val date = request.toGregorianDate()
        val year = date.year
        
        // Validate year range
        if (year <= 1000) {
            throw IllegalArgumentException("Published year must be greater than 1000")
        }
        if (year > LocalDate.now().year) {
            throw IllegalArgumentException("Published year cannot be in the future")
        }
        
        val book = Book(
            title = request.title.trim(),
            author = request.author.trim(),
            publishedDate = date,
            status = request.status
        )
        
        return bookRepository.save(book)
    }

    /**
     * Update book status
     */
    fun updateBookStatus(id: Long, status: BookStatus): Book {
        val book = bookRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Book not found with id: $id") }
        
        val updatedBook = book.copy(status = status)
        return bookRepository.save(updatedBook)
    }
}
