package com.asmt.bookmanagementsystem.service

import com.asmt.bookmanagementsystem.config.ApplicationConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.Status
import java.sql.Connection
import java.sql.DatabaseMetaData
import javax.sql.DataSource
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataSourceHealthIndicatorTest {

    @Mock
    private lateinit var dataSource: DataSource

    @Mock
    private lateinit var applicationConfig: ApplicationConfig

    @Mock
    private lateinit var connection: Connection

    @Mock
    private lateinit var metaData: DatabaseMetaData

    @InjectMocks
    private lateinit var dataSourceHealthIndicator: DataSourceHealthIndicator

    @BeforeEach
    fun setUp() {
        `when`(applicationConfig.database).thenReturn("MySQL")
        `when`(dataSource.connection).thenReturn(connection)
        `when`(connection.metaData).thenReturn(metaData)
        `when`(metaData.url).thenReturn("jdbc:mysql://localhost:3306/bookdb")
        `when`(metaData.driverName).thenReturn("com.mysql.cj.jdbc.Driver")
        `when`(metaData.driverVersion).thenReturn("8.0.43")
    }

    @Test
    fun `health should return UP when connection is valid`() {
        `when`(connection.isValid(5)).thenReturn(true)

        val result = dataSourceHealthIndicator.health()

        assertEquals(Status.UP, result.status)
        assertEquals("MySQL", result.details["database"])
        assertEquals("OK", result.details["connection"])
        assertEquals("jdbc:mysql://localhost:3306/bookdb", result.details["url"])
        assertEquals("com.mysql.cj.jdbc.Driver", result.details["driver"])
        assertEquals("8.0.43", result.details["version"])
    }

    @Test
    fun `health should return DOWN when connection is invalid`() {
        `when`(connection.isValid(5)).thenReturn(false)

        val result = dataSourceHealthIndicator.health()

        assertEquals(Status.DOWN, result.status)
        assertEquals("MySQL", result.details["database"])
        assertEquals("Invalid", result.details["connection"])
    }

    @Test
    fun `health should return DOWN when connection throws exception`() {
        `when`(dataSource.connection).thenThrow(RuntimeException("Connection failed"))

        val result = dataSourceHealthIndicator.health()

        assertEquals(Status.DOWN, result.status)
        assertEquals("MySQL", result.details["database"])
        assertEquals("Connection failed", result.details["error"])
    }
}
