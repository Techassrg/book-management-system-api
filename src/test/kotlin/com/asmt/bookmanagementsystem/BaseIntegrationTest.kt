package com.asmt.bookmanagementsystem

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:mysql://localhost:3306/bookdb_test?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC",
    "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
    "spring.datasource.username=root",
    "spring.datasource.password=password",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
    "spring.flyway.enabled=false"
])
@Transactional
abstract class BaseIntegrationTest
