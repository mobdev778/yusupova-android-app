package com.github.mobdev778.yusupova.domain.model.verses

data class Book(
    val id: Int,
    val title: String,
    val verseCount: Int,
    val verses: List<Verse>
)
