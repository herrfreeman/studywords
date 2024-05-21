package ru.blackmesa.studywords.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 3,
    entities = [DictEntity::class, WordEntity::class, WordInDictEntity::class, WordTranslateEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao
}

