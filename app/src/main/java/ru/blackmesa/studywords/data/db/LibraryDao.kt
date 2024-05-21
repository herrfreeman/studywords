package ru.blackmesa.studywords.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.blackmesa.studywords.data.models.WordWithTranslate

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


    @Query("""SELECT wordindict_table.wordid as wordid,
 words_table.word as word,
 wordtranslate_table.translate as translate
 FROM wordindict_table
join words_table on words_table.id = wordindict_table.wordid
join wordtranslate_table on words_table.id = wordtranslate_table.wordid
WHERE dictid = :dictId""")
    fun getWords(dictId: Int): List<WordWithTranslate>

}

