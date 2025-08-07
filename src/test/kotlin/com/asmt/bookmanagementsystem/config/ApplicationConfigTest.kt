package com.asmt.bookmanagementsystem.config

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import jakarta.validation.Validation
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ApplicationConfigTest {

    private lateinit var applicationConfig: ApplicationConfig
    private lateinit var validator: LocalValidatorFactoryBean

    @BeforeEach
    fun setUp() {
        applicationConfig = ApplicationConfig()
        validator = LocalValidatorFactoryBean()
        validator.setValidationMessageSource(org.springframework.context.support.ResourceBundleMessageSource())
        validator.afterPropertiesSet()
    }

    @Test
    fun `should have default values`() {
        // Then
        assertEquals("Book Management System", applicationConfig.name)
        assertEquals("1.0.0", applicationConfig.version)
        assertEquals("MySQL", applicationConfig.database)
    }

    @Test
    fun `should validate supported database types`() {
        // Given
        val validDatabases = listOf("MySQL", "PostgreSQL", "Oracle", "H2", "SQLite")
        
        // When & Then
        validDatabases.forEach { database ->
            applicationConfig.database = database
            val violations = validator.validate(applicationConfig)
            assertEquals(0, violations.size, "Database $database should be valid")
        }
    }

    @Test
    fun `should reject invalid database type`() {
        // Given
        applicationConfig.database = "InvalidDB"
        
        // When & Then
        val violations = validator.validate(applicationConfig)
        assertEquals(1, violations.size)
        assert(violations.any { it.message?.contains("Database must be one of") == true })
    }

    @Test
    fun `should reject blank database type`() {
        // Given
        applicationConfig.database = ""
        
        // When & Then
        val violations = validator.validate(applicationConfig)
        assert(violations.isNotEmpty())
    }

    @Test
    fun `should validate version format`() {
        // Given
        val validVersions = listOf("1.0.0", "2.1.3", "10.5.2")
        
        // When & Then
        validVersions.forEach { version ->
            applicationConfig.version = version
            val violations = validator.validate(applicationConfig)
            assertEquals(0, violations.size, "Version $version should be valid")
        }
    }

    @Test
    fun `should reject invalid version format`() {
        // Given
        val invalidVersions = listOf("1.0", "2.1.3.4", "v1.0.0", "1.0.0-SNAPSHOT")
        
        // When & Then
        invalidVersions.forEach { version ->
            applicationConfig.version = version
            val violations = validator.validate(applicationConfig)
            assertEquals(1, violations.size, "Version $version should be invalid")
            assert(violations.any { it.message?.contains("format x.y.z") == true })
        }
    }

    @Test
    fun `should reject blank version`() {
        // Given
        applicationConfig.version = ""
        
        // When & Then
        val violations = validator.validate(applicationConfig)
        assert(violations.isNotEmpty())
    }

    @Test
    fun `should reject blank name`() {
        // Given
        applicationConfig.name = ""
        
        // When & Then
        val violations = validator.validate(applicationConfig)
        assertEquals(1, violations.size)
        assert(violations.any { it.message?.contains("cannot be blank") == true })
    }

    @Test
    fun `should have supported databases constant`() {
        // Then
        val supportedDatabases = ApplicationConfig.getSupportedDatabases()
        assertNotNull(supportedDatabases)
        assertEquals(5, supportedDatabases.size)
        assertEquals(setOf("MySQL", "PostgreSQL", "Oracle", "H2", "SQLite"), 
                    supportedDatabases)
    }

    @Test
    fun `should validate supported database helper method`() {
        // Given
        val validDatabases = listOf("MySQL", "PostgreSQL", "Oracle", "H2", "SQLite")
        val invalidDatabases = listOf("InvalidDB", "MongoDB", "Redis", "")
        
        // When & Then
        validDatabases.forEach { database ->
            assert(ApplicationConfig.isSupportedDatabase(database))
        }
        
        invalidDatabases.forEach { database ->
            assert(!ApplicationConfig.isSupportedDatabase(database))
        }
    }

    @Test
    fun `should get supported databases as string`() {
        // When
        val result = ApplicationConfig.getSupportedDatabasesAsString()
        
        // Then
        assertEquals("MySQL, PostgreSQL, Oracle, H2, SQLite", result)
    }

    @Test
    fun `should get supported database enum by display name`() {
        // When & Then
        assertEquals(SupportedDatabase.MYSQL, ApplicationConfig.getSupportedDatabase("MySQL"))
        assertEquals(SupportedDatabase.POSTGRESQL, ApplicationConfig.getSupportedDatabase("PostgreSQL"))
        assertEquals(SupportedDatabase.ORACLE, ApplicationConfig.getSupportedDatabase("Oracle"))
        assertEquals(SupportedDatabase.H2, ApplicationConfig.getSupportedDatabase("H2"))
        assertEquals(SupportedDatabase.SQLITE, ApplicationConfig.getSupportedDatabase("SQLite"))
        
        assertNull(ApplicationConfig.getSupportedDatabase("InvalidDB"))
        assertNull(ApplicationConfig.getSupportedDatabase("MongoDB"))
    }

    @Test
    fun `should validate version format helper method`() {
        // Given
        val validVersions = listOf("1.0.0", "2.1.3", "10.5.2")
        val invalidVersions = listOf("1.0", "2.1.3.4", "v1.0.0", "1.0.0-SNAPSHOT")
        
        // When & Then
        validVersions.forEach { version ->
            assert(ApplicationConfig.validateVersionFormat(version))
        }
        
        invalidVersions.forEach { version ->
            assert(!ApplicationConfig.validateVersionFormat(version))
        }
    }

    @Test
    fun `should have timestamp format constant`() {
        // Then
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", ApplicationConfig.TIMESTAMP_FORMAT)
    }

    @Test
    fun `should have health message constant`() {
        // Then
        assertEquals("Book Management System is running", ApplicationConfig.HEALTH_MESSAGE)
    }


}
