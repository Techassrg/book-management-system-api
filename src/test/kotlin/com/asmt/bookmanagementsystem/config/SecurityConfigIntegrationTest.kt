package com.asmt.bookmanagementsystem.config

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityConfigIntegrationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should allow access to health endpoint without authentication`() {
        val response = restTemplate.getForEntity("/actuator/health", String::class.java)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body?.contains("UP") == true)
    }

    @Test
    fun `should allow access to swagger-ui without authentication`() {
        val response = restTemplate.getForEntity("/swagger-ui/index.html", String::class.java)
        assert(response.statusCode == HttpStatus.OK)
    }

    @Test
    fun `should require authentication for books endpoint`() {
        val response = restTemplate.getForEntity("/api/books", String::class.java)
        assert(response.statusCode == HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `should require authentication for POST books endpoint`() {
        val response = restTemplate.postForEntity("/api/books", "{}", String::class.java)
        assert(response.statusCode == HttpStatus.UNAUTHORIZED)
    }
}
