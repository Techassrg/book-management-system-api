package com.asmt.bookmanagementsystem.config

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

/**
 * Enum representing supported database types
 */
enum class SupportedDatabase(val displayName: String, val driverClass: String, val urlPattern: String) {
    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://{host}:{port}/{database}"),
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver", "jdbc:postgresql://{host}:{port}/{database}"),
    ORACLE("Oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@{host}:{port}:{database}"),
    H2("H2", "org.h2.Driver", "jdbc:h2:{host}:{port}/{database}"),
    SQLITE("SQLite", "org.sqlite.JDBC", "jdbc:sqlite:{database}");

    companion object {
        fun getSupportedNames(): Set<String> = values().map { it.displayName }.toSet()
        fun getSupportedNamesAsString(): String = values().joinToString(", ") { it.displayName }
        fun fromDisplayName(displayName: String): SupportedDatabase? {
            return values().find { it.displayName.equals(displayName, ignoreCase = true) }
        }
        fun isSupported(displayName: String): Boolean {
            return fromDisplayName(displayName) != null
        }
    }
}

@Configuration
@ConfigurationProperties(prefix = "app")
@Validated
class ApplicationConfig {

    @NotBlank(message = "Application name cannot be blank")
    var name: String = "Book Management System"

    @NotBlank(message = "Application version cannot be blank")
    @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "Version must be in format x.y.z")
    var version: String = "1.0.0"

    @NotBlank(message = "Database type cannot be blank")
    @Pattern(regexp = "^(MySQL|PostgreSQL|Oracle|H2|SQLite)$", message = "Database must be one of: MySQL, PostgreSQL, Oracle, H2, SQLite")
    var database: String = "MySQL"

    companion object {
        const val TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val HEALTH_MESSAGE = "Book Management System is running"
        
        fun isSupportedDatabase(database: String): Boolean = SupportedDatabase.isSupported(database)
        fun getSupportedDatabasesAsString(): String = SupportedDatabase.getSupportedNamesAsString()
        fun validateVersionFormat(version: String): Boolean {
            return version.matches(Regex("^\\d+\\.\\d+\\.\\d+$"))
        }
        fun getSupportedDatabase(database: String): SupportedDatabase? {
            return SupportedDatabase.fromDisplayName(database)
        }
        fun getSupportedDatabases(): Set<String> = SupportedDatabase.getSupportedNames()
    }
}
