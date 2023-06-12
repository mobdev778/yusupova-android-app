package com.github.mobdev778.yusupova.data.repository.verses.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.BooksDao
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.VerseLinesDao
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.VersesDao
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBook
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomBookItem
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerse
import com.github.mobdev778.yusupova.data.repository.verses.local.model.RoomVerseLine

@Database(
    entities = [RoomBook::class, RoomVerse::class, RoomVerseLine::class ],
    version = RoomVerseDatabase.DATABASE_VERSION,
    exportSchema = false,
    views = [ RoomBookItem::class ]
)
internal abstract class RoomVerseDatabase: RoomDatabase() {

    abstract fun booksDao(): BooksDao
    abstract fun versesDao(): VersesDao
    abstract fun verseLinesDao(): VerseLinesDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}
