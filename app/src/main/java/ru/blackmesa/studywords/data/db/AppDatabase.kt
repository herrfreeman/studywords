package ru.blackmesa.studywords.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 6,
    entities = [
        DictEntity::class,
        WordEntity::class,
        WordInDictEntity::class,
        WordTranslateEntity::class,
        ProgressEntity::class,
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun libraryDao(): LibraryDao
}

