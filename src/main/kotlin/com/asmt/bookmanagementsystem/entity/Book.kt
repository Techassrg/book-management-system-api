package com.asmt.bookmanagementsystem.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "books",
    indexes = [
        Index(name = "idx_author", columnList = "author"),
        Index(name = "idx_published_date", columnList = "published_date"),
        Index(name = "idx_status", columnList = "status"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
data class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(nullable = false, length = 255)
    val author: String,

    @Column(name = "published_date", nullable = false)
    val publishedDate: LocalDate,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: BookStatus = BookStatus.AVAILABLE,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}

enum class BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    MAINTENANCE
}
