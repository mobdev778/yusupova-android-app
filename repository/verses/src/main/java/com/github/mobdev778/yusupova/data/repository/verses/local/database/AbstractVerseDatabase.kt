package com.github.mobdev778.yusupova.data.repository.verses.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.BooksDao
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.VerseLinesDao
import com.github.mobdev778.yusupova.data.repository.verses.local.dao.VersesDao
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalBook
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalBookItem
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalVerse
import com.github.mobdev778.yusupova.data.repository.verses.local.model.LocalVerseLine

@Database(
    entities = [LocalBook::class, LocalVerse::class, LocalVerseLine::class ],
    version = AbstractVerseDatabase.DATABASE_VERSION,
    exportSchema = false,
    views = [ LocalBookItem::class ]
)
internal abstract class AbstractVerseDatabase: RoomDatabase() {

    abstract fun booksDao(): BooksDao
    abstract fun versesDao(): VersesDao
    abstract fun verseLinesDao(): VerseLinesDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}
