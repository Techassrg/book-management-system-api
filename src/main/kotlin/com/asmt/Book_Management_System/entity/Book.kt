package com.asmt.Book_Management_System.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "books",
    indexes = [
        Index(name = "idx_author", columnList = "author"),
        Index(name = "idx_published_date", columnList = "published_date")
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
    val publishedDate: LocalDate
)
