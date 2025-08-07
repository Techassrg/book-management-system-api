package com.asmt.bookmanagementsystem.repository

import com.asmt.bookmanagementsystem.BaseIntegrationTest
import com.asmt.bookmanagementsystem.entity.Book
import com.asmt.bookmanagementsystem.entity.BookStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var bookRepository: BookRepository



    private lateinit var book1: Book
    private lateinit var book2: Book
    private lateinit var book3: Book

    @BeforeEach
    fun setUp() {
        bookRepository.deleteAll()

        book1 = Book(
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            publishedDate = LocalDate.of(1925, 4, 10),
            status = BookStatus.AVAILABLE
        )

        book2 = Book(
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            publishedDate = LocalDate.of(1960, 7, 11),
            status = BookStatus.BORROWED
        )

        book3 = Book(
            title = "1984",
            author = "George Orwell",
            publishedDate = LocalDate.of(1949, 6, 8),
            status = BookStatus.AVAILABLE
        )

        book1 = bookRepository.save(book1)
        book2 = bookRepository.save(book2)
        book3 = bookRepository.save(book3)
    }

    @Test
    fun `should find books by author`() {
        val books = bookRepository.findByAuthor("F. Scott Fitzgerald")

        assertEquals(1, books.size)
        assertEquals("The Great Gatsby", books[0].title)
        assertEquals("F. Scott Fitzgerald", books[0].author)
    }

    @Test
    fun `should find books by author containing case insensitive`() {
        val books = bookRepository.findByAuthorContainingIgnoreCase("fitzgerald")

        assertEquals(1, books.size)
        assertEquals("The Great Gatsby", books[0].title)
        assertEquals("F. Scott Fitzgerald", books[0].author)
    }

    @Test
    fun `should find books by author containing case insensitive uppercase`() {
        // When
        val books = bookRepository.findByAuthorContainingIgnoreCase("FITZGERALD")

        // Then
        assertEquals(1, books.size)
        assertEquals("The Great Gatsby", books[0].title)
    }

    @Test
    fun `should find books by status`() {
        // When
        val availableBooks = bookRepository.findByStatus(BookStatus.AVAILABLE)
        val borrowedBooks = bookRepository.findByStatus(BookStatus.BORROWED)

        // Then
        assertEquals(2, availableBooks.size)
        assertEquals(1, borrowedBooks.size)
        assertEquals("To Kill a Mockingbird", borrowedBooks[0].title)
    }

    @Test
    fun `should find books by author and status`() {
        val books = bookRepository.findByAuthorAndStatus("Harper Lee", BookStatus.BORROWED)

        assertTrue(books.isNotEmpty())
        val book = books.first()
        assertEquals("To Kill a Mockingbird", book.title)
        assertEquals("Harper Lee", book.author)
        assertEquals(BookStatus.BORROWED, book.status)
    }

    @Test
    fun `should return empty list when no books match author`() {
        // When
        val books = bookRepository.findByAuthor("Unknown Author")

        // Then
        assertTrue(books.isEmpty())
    }

    @Test
    fun `should return empty list when no books match status`() {
        // When
        val books = bookRepository.findByStatus(BookStatus.RESERVED)

        // Then
        assertTrue(books.isEmpty())
    }

    @Test
    fun `should return empty list when no books match author and status combination`() {
        // When
        val books = bookRepository.findByAuthorAndStatus("F. Scott Fitzgerald", BookStatus.BORROWED)

        // Then
        assertTrue(books.isEmpty())
    }

    @Test
    fun `should save book with audit fields`() {
        // Given
        val newBook = Book(
            title = "Test Book",
            author = "Test Author",
            publishedDate = LocalDate.of(2024, 1, 1),
            status = BookStatus.AVAILABLE
        )

        // When
        val savedBook = bookRepository.save(newBook)

        // Then
        assertNotNull(savedBook.id)
        assertNotNull(savedBook.createdAt)
        assertNotNull(savedBook.updatedAt)
        assertEquals("Test Book", savedBook.title)
        assertEquals("Test Author", savedBook.author)
    }

    @Test
    fun `should update book and update audit fields`() {
        // Given
        val bookToUpdate = book1.copy(status = BookStatus.BORROWED)

        // When
        val updatedBook = bookRepository.save(bookToUpdate)

        // Then
        assertEquals(BookStatus.BORROWED, updatedBook.status)
        assertNotNull(updatedBook.updatedAt)
        // updatedAt should be equal to or after createdAt
        assertTrue(updatedBook.updatedAt!!.isAfter(updatedBook.createdAt!!) || 
                  updatedBook.updatedAt!!.isEqual(updatedBook.createdAt!!))
    }

    @Test
    fun `should find all books ordered by published date desc for author`() {
        // Given - Add another book by same author with different date
        val book4 = Book(
            title = "Tender Is the Night",
            author = "F. Scott Fitzgerald",
            publishedDate = LocalDate.of(1934, 4, 12),
            status = BookStatus.AVAILABLE
        )
        bookRepository.save(book4)

        // When
        val books = bookRepository.findByAuthor("F. Scott Fitzgerald")

        // Then
        assertEquals(2, books.size)
        // Should be ordered by publishedDate DESC
        assertEquals("Tender Is the Night", books[0].title) // 1934
        assertEquals("The Great Gatsby", books[1].title) // 1925
    }

    @Test
    fun `should find books by author containing with partial match`() {
        val books = bookRepository.findByAuthorContainingIgnoreCase("lee")

        assertTrue(books.isNotEmpty())
        val book = books.first()
        assertEquals("To Kill a Mockingbird", book.title)
        assertEquals("Harper Lee", book.author)
    }

    @Test
    fun `should find books by status ordered by created date desc`() {
        // When
        val availableBooks = bookRepository.findByStatus(BookStatus.AVAILABLE)

        // Then
        assertEquals(2, availableBooks.size)
        // Should be ordered by createdAt DESC (newest first)
        assertTrue(availableBooks[0].createdAt!!.isAfter(availableBooks[1].createdAt!!) || 
                  availableBooks[0].createdAt!!.isEqual(availableBooks[1].createdAt!!))
    }
}
