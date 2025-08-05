package com.asmt.Book_Management_System

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookApiIntegrationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun `should create book successfully`() {
		val payload = """
            {
              "title": "Kotlin Programming",
              "author": "Techasit Srirueng",
              "publishedDate": "2568-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.title").value("Kotlin Programming"))
			.andExpect(jsonPath("$.author").value("Techasit Srirueng"))
			.andExpect(jsonPath("$.publishedDate").value("2025-01-01"))
	}

	@Test
	fun `should get books by author`() {
		// สร้างหนังสือก่อน
		val payload = """
            {
              "title": "Kotlin Spring Boot",
              "author": "Techattara Srijaisuk",
              "publishedDate": "2567-06-15"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		).andExpect(status().isCreated)

		// ค้นหาหนังสือตามผู้เขียน
		mockMvc.perform(
			get("/books")
				.with(httpBasic("user", "password"))
				.param("author", "Techattara Srijaisuk")
		)
			.andExpect(status().isOk)
			.andExpect(jsonPath("$[0].title").value("Kotlin Spring Boot"))
			.andExpect(jsonPath("$[0].author").value("Techattara Srijaisuk"))
	}

	@Test
	fun `should reject book with empty title`() {
		val payload = """
            {
              "title": "",
              "author": "Techasit Srirueng",
              "publishedDate": "2568-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		).andExpect(status().isBadRequest)
	}

	@Test
	fun `should reject book with empty author`() {
		val payload = """
            {
              "title": "Ascend Money",
              "author": "",
              "publishedDate": "2568-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		).andExpect(status().isBadRequest)
	}

	@Test
	fun `should reject book with invalid year`() {
		val payload = """
            {
              "title": "Test Book",
              "author": "Max Versteppen",
              "publishedDate": "999-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		).andExpect(status().isBadRequest)
	}

	@Test
	fun `should reject request without authentication`() {
		val payload = """
            {
              "title": "Test Book",
              "author": "Luwis Hamilton",
              "publishedDate": "2568-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		).andExpect(status().isUnauthorized)
	}

	@Test
	fun `should convert buddhist calendar to gregorian correctly`() {
		val payload = """
            {
              "title": "Buddhist Calendar Test",
              "author": "Test Author",
              "publishedDate": "2567-01-01"
            }
        """.trimIndent()

		mockMvc.perform(
			post("/books")
				.with(httpBasic("user", "password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload)
		)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.publishedDate").value("2024-01-01"))
	}
}
