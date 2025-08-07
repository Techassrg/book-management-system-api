package com.asmt.bookmanagementsystem.service

import com.asmt.bookmanagementsystem.config.ApplicationConfig
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class DataSourceHealthIndicator(
    private val dataSource: DataSource,
    private val applicationConfig: ApplicationConfig
) : HealthIndicator {

    override fun health(): Health {
        return try {
            dataSource.connection.use { connection ->
                if (connection.isValid(5)) {
                    Health.up()
                        .withDetail("database", applicationConfig.database)
                        .withDetail("connection", "OK")
                        .withDetail("url", connection.metaData.url)
                        .withDetail("driver", connection.metaData.driverName)
                        .withDetail("version", connection.metaData.driverVersion)
                        .build()
                } else {
                    Health.down()
                        .withDetail("database", applicationConfig.database)
                        .withDetail("connection", "Invalid")
                        .build()
                }
            }
        } catch (e: Exception) {
            Health.down()
                .withDetail("database", applicationConfig.database)
                .withDetail("error", e.message)
                .build()
        }
    }
}
