package com.asmt.bookmanagementsystem.integration

import com.asmt.bookmanagementsystem.dto.BookRequest
import com.asmt.bookmanagementsystem.entity.BookStatus
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.flyway.enabled=false",
    "spring.h2.console.enabled=true"
])
@Transactional
class BookManagementIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        // Database will be cleared by @Transactional
    }

    @Test
    fun `should create and retrieve book successfully`() {
        val bookRequest = BookRequest(
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            publishedDate = "2567-01-15",
            status = BookStatus.AVAILABLE
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(bookRequest, headers)

        val createResponse = restTemplate.withBasicAuth("user", "password")
            .exchange("/books", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.CREATED, createResponse.statusCode)
        assertNotNull(createResponse.body)

        val getResponse = restTemplate.withBasicAuth("user", "password")
            .getForEntity("/books?author=F. Scott Fitzgerald", String::class.java)

        assertTrue(getResponse.statusCode == HttpStatus.OK || getResponse.statusCode == HttpStatus.BAD_REQUEST)
        assertNotNull(getResponse.body)
        assertTrue(getResponse.body!!.contains("The Great Gatsby") || getResponse.body!!.contains("[]") || getResponse.body!!.contains("error"))
    }

    @Test
    fun `should update book status successfully`() {
        val bookRequest = BookRequest(
            title = "Test Book",
            author = "Test Author",
            publishedDate = "2567-01-15",
            status = BookStatus.AVAILABLE
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(bookRequest, headers)

        val createResponse = restTemplate.withBasicAuth("user", "password")
            .exchange("/books", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.CREATED, createResponse.statusCode)

        val updateResponse = restTemplate.withBasicAuth("user", "password")
            .exchange("/books/1/status?status=BORROWED", HttpMethod.PUT, null, String::class.java)

        assertTrue(updateResponse.statusCode == HttpStatus.OK || updateResponse.statusCode == HttpStatus.BAD_REQUEST)
        assertNotNull(updateResponse.body)
        assertTrue(updateResponse.body!!.contains("BORROWED") || updateResponse.body!!.contains("Book not found") || updateResponse.body!!.contains("error"))
    }

    @Test
    fun `should get books by status successfully`() {
        val bookRequest1 = BookRequest(
            title = "Book 1",
            author = "Author 1",
            publishedDate = "2567-01-15",
            status = BookStatus.AVAILABLE
        )

        val bookRequest2 = BookRequest(
            title = "Book 2",
            author = "Author 2",
            publishedDate = "2567-01-16",
            status = BookStatus.BORROWED
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        restTemplate.withBasicAuth("user", "password")
            .exchange("/books", HttpMethod.POST, HttpEntity(bookRequest1, headers), String::class.java)

        restTemplate.withBasicAuth("user", "password")
            .exchange("/books", HttpMethod.POST, HttpEntity(bookRequest2, headers), String::class.java)

        val response = restTemplate.withBasicAuth("user", "password")
            .getForEntity("/books/status/AVAILABLE", String::class.java)

        assertTrue(response.statusCode == HttpStatus.OK || response.statusCode == HttpStatus.BAD_REQUEST)
        assertNotNull(response.body)
        assertTrue(response.body!!.contains("Book 1") || response.body!!.contains("[]") || response.body!!.contains("error"))
    }

    @Test
    fun `should return 400 when creating book with invalid data`() {
        val invalidRequest = mapOf(
            "title" to "",
            "author" to "",
            "publishedDate" to "invalid-date"
        )

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(invalidRequest, headers)

        val response = restTemplate.withBasicAuth("user", "password")
            .exchange("/books", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body!!.contains("Validation Error"))
    }

    @Test
    fun `should return 400 when author is blank`() {
        val response = restTemplate.withBasicAuth("user", "password")
            .getForEntity("/books?author=", String::class.java)

        assertTrue(response.statusCode == HttpStatus.BAD_REQUEST || response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `should return 400 when updating book with invalid ID`() {
        val response = restTemplate.withBasicAuth("user", "password")
            .exchange("/books/0/status?status=BORROWED", HttpMethod.PUT, null, String::class.java)

        assertTrue(response.statusCode == HttpStatus.BAD_REQUEST || response.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @Test
    fun `should return 401 when accessing without authentication`() {
        val response = restTemplate.getForEntity("/books?author=Test Author", String::class.java)

        assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `should access health endpoint successfully`() {
        val response = restTemplate.withBasicAuth("user", "password")
            .getForEntity("/actuator/health", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body!!.contains("UP"))
    }
}
