package com.asmt.bookmanagementsystem.service

import com.asmt.bookmanagementsystem.BaseIntegrationTest
import com.asmt.bookmanagementsystem.dto.BookRequest
import com.asmt.bookmanagementsystem.entity.Book
import com.asmt.bookmanagementsystem.entity.BookStatus
import com.asmt.bookmanagementsystem.repository.BookRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookServiceTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var bookRepository: BookRepository

    @BeforeEach
    fun setUp() {
        bookRepository.deleteAll()
    }

    @Test
    fun `should get books by author successfully`() {
        val author = "F. Scott Fitzgerald"
        val book1 = Book(title = "The Great Gatsby", author = author, publishedDate = LocalDate.of(1925, 4, 10))
        val book2 = Book(title = "Tender Is the Night", author = author, publishedDate = LocalDate.of(1934, 4, 12))
        bookRepository.save(book1)
        bookRepository.save(book2)

        val result = bookService.getBooksByAuthor(author)

        assertEquals(2, result.size)
        val titles = result.map { it.title }.sorted()
        assertEquals("Tender Is the Night", titles[0])
        assertEquals("The Great Gatsby", titles[1])
    }

    @Test
    fun `should throw exception when author is blank`() {
        val blankAuthor = "   "

        val exception = assertThrows<IllegalArgumentException> {
            bookService.getBooksByAuthor(blankAuthor)
        }
        assertEquals("Author name cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when author is empty`() {
        val emptyAuthor = ""

        val exception = assertThrows<IllegalArgumentException> {
            bookService.getBooksByAuthor(emptyAuthor)
        }
        assertEquals("Author name cannot be empty", exception.message)
    }

    @Test
    fun `should trim author name before querying`() {
        val authorWithSpaces = "  F. Scott Fitzgerald  "
        val trimmedAuthor = "F. Scott Fitzgerald"
        val book = Book(title = "The Great Gatsby", author = trimmedAuthor, publishedDate = LocalDate.of(1925, 4, 10))
        bookRepository.save(book)

        val result = bookService.getBooksByAuthor(authorWithSpaces)

        assertEquals(1, result.size)
        assertEquals("The Great Gatsby", result[0].title)
    }

    @Test
    fun `should get books by status successfully`() {
        val status = BookStatus.AVAILABLE
        val book1 = Book(title = "Book 1", author = "Author 1", publishedDate = LocalDate.of(2020, 1, 1), status = status)
        val book2 = Book(title = "Book 2", author = "Author 2", publishedDate = LocalDate.of(2021, 1, 1), status = status)
        bookRepository.save(book1)
        bookRepository.save(book2)

        val result = bookService.getBooksByStatus(status)

        assertEquals(2, result.size)
        assertEquals(status, result[0].status)
        assertEquals(status, result[1].status)
    }

    @Test
    fun `should save book successfully with valid request`() {
        val request = BookRequest(
            title = "Test Book",
            author = "Test Author",
            publishedDate = "2567-01-15",
            status = BookStatus.AVAILABLE
        )

        val result = bookService.saveBook(request)

        assertNotNull(result)
        assertEquals("Test Book", result.title)
        assertEquals("Test Author", result.author)
        assertEquals(LocalDate.of(2024, 1, 15), result.publishedDate)
        assertEquals(BookStatus.AVAILABLE, result.status)
    }

    @Test
    fun `should trim title and author when saving book`() {
        // Given
        val request = BookRequest(
            title = "  Test Book  ",
            author = "  Test Author  ",
            publishedDate = "2567-01-15",
            status = BookStatus.AVAILABLE
        )

        // When
        val result = bookService.saveBook(request)

        // Then
        assertEquals("Test Book", result.title)
        assertEquals("Test Author", result.author)
    }

    @Test
    fun `should throw exception when published year is too old`() {
        // Given
        val request = BookRequest(
            title = "Old Book",
            author = "Old Author",
            publishedDate = "1000-01-01", // Year 1000
            status = BookStatus.AVAILABLE
        )

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            bookService.saveBook(request)
        }
        assertEquals("Published year must be greater than 1000", exception.message)
    }

    @Test
    fun `should throw exception when published year is in future`() {
        // Given
        val futureYear = LocalDate.now().year + 1
        val request = BookRequest(
            title = "Future Book",
            author = "Future Author",
            publishedDate = "${futureYear + 543}-01-01", // Buddhist calendar
            status = BookStatus.AVAILABLE
        )

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            bookService.saveBook(request)
        }
        assertEquals("Published year cannot be in the future", exception.message)
    }

    @Test
    fun `should update book status successfully`() {
        // Given
        val newStatus = BookStatus.BORROWED
        val existingBook = Book(
            title = "Test Book",
            author = "Test Author",
            publishedDate = LocalDate.of(2024, 1, 1),
            status = BookStatus.AVAILABLE
        )
        val savedBook = bookRepository.save(existingBook)

        // When
        val result = bookService.updateBookStatus(savedBook.id, newStatus)

        // Then
        assertEquals(newStatus, result.status)
        assertEquals(savedBook.id, result.id)
    }

    @Test
    fun `should throw exception when updating non-existent book`() {
        // Given
        val bookId = 999L
        val newStatus = BookStatus.BORROWED

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            bookService.updateBookStatus(bookId, newStatus)
        }
        assertEquals("Book not found with id: $bookId", exception.message)
    }

    @Test
    fun `should handle Buddhist calendar conversion correctly`() {
        // Given
        val request = BookRequest(
            title = "Buddhist Calendar Test",
            author = "Test Author",
            publishedDate = "2567-12-31", // Buddhist calendar
            status = BookStatus.AVAILABLE
        )

        // When
        val result = bookService.saveBook(request)

        // Then
        assertEquals(LocalDate.of(2024, 12, 31), result.publishedDate)
    }

    @Test
    fun `should handle leap year in Buddhist calendar`() {
        // Given
        val request = BookRequest(
            title = "Leap Year Test",
            author = "Test Author",
            publishedDate = "2567-02-29", // Buddhist calendar leap year
            status = BookStatus.AVAILABLE
        )

        // When
        val result = bookService.saveBook(request)

        // Then
        assertEquals(LocalDate.of(2024, 2, 29), result.publishedDate)
    }

    @Test
    fun `should handle invalid Buddhist date format`() {
        // Given
        val request = BookRequest(
            title = "Invalid Date Test",
            author = "Test Author",
            publishedDate = "invalid-date",
            status = BookStatus.AVAILABLE
        )

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            bookService.saveBook(request)
        }
        assertTrue(exception.message!!.contains("Invalid date format") || 
                  exception.message!!.contains("Invalid Buddhist date"))
    }

    @Test
    fun `should handle all BookStatus values correctly`() {
        // Given
        val statuses = listOf(BookStatus.AVAILABLE, BookStatus.BORROWED, BookStatus.RESERVED, BookStatus.MAINTENANCE)
        
        statuses.forEach { status ->
            val request = BookRequest(
                title = "Test Book $status",
                author = "Test Author",
                publishedDate = "2567-01-01",
                status = status
            )

            // When
            val result = bookService.saveBook(request)

            // Then
            assertEquals(status, result.status)
        }
    }
}
