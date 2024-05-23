package ru.blackmesa.studywords.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.blackmesa.studywords.data.models.WordData

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDict(entities: List<DictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWords(entities: List<WordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWordInList(entities: List<WordInDictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTranslate(entities: List<WordTranslateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgress(entities: List<ProgressEntity>)

    @Query("SELECT version FROM dict_table ORDER BY version DESC LIMIT 1;")
    fun getDictVersion(): List<Long>

    @Query("SELECT version FROM words_table ORDER BY version DESC LIMIT 1;")
    fun getWordsVersion(): List<Long>

    @Query("SELECT version FROM wordindict_table ORDER BY version DESC LIMIT 1;")
    fun getWordInDictVersion(): List<Long>

    @Query("SELECT version FROM wordtranslate_table ORDER BY version DESC LIMIT 1;")
    fun getTranslateVersion(): List<Long>

    @Query("SELECT * FROM dict_table ORDER BY orderfield")
    fun getDict(): List<DictEntity>

    @Query("SELECT * FROM wordindict_table WHERE dictid = :dictId")
    fun getWordsInDict(dictId: Int): List<WordInDictEntity>


    @Query("""SELECT wordindict_table.wordid AS wordid,
        words_table.word AS word,
        wordtranslate_table.translate AS translate,
        IFNULL(progress_table.answerdate,0) AS answerdate,
        IFNULL(progress_table.status, 0)  AS baseprogress,
        IFNULL(progress_table.status,0) AS newprogress,
        0 AS answerdate
        FROM wordindict_table
        JOIN words_table ON words_table.id = wordindict_table.wordid
        JOIN wordtranslate_table ON words_table.id = wordtranslate_table.wordid
        LEFT JOIN progress_table ON progress_table.wordid = wordindict_table.wordid AND progress_table.userid = :userId
        WHERE dictid = :dictId""")
    fun getWords(dictId: Int, userId: Int): List<WordData>

}

