package ru.blackmesa.studywords.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.DraftWordData
import ru.blackmesa.studywords.data.models.TranslateData
import ru.blackmesa.studywords.data.models.WordAndTranslatesData

@Dao
interface LibraryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDict(entities: List<DictEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDict(entities: List<DictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWords(entities: List<WordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWordInList(entities: List<WordInDictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTranslate(entities: List<WordTranslateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPriorityTranslate(entities: List<PriorityTranslateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgress(entities: List<ProgressEntity>)

    @Query("SELECT version FROM dict_table ORDER BY version DESC LIMIT 1;")
    fun getDictVersion(): List<Long>

    @Query("SELECT version FROM progress_table WHERE userid = :userid AND touched = 0 ORDER BY version DESC LIMIT 1;")
    fun getProgressVersion(userid: Int): List<Long>

    @Query("DELETE FROM dict_table;")
    fun deleteDicts()

    @Query("DELETE FROM words_table;")
    fun deleteWords()

    @Query("DELETE FROM wordindict_table;")
    fun deleteWordInDict()

    @Query("DELETE FROM wordtranslate_table;")
    fun deleteTranslate()

    @Query("DELETE FROM progress_table;")
    fun deleteProgress()

    @Query("SELECT * FROM progress_table WHERE userid = :userid AND touched = 1;")
    fun getLocalProgress(userid: Int): List<ProgressEntity>

    @Query("SELECT * FROM dict_table ORDER BY orderfield")
    fun getDict(): List<DictEntity>

    @Query("SELECT * FROM dict_table WHERE id = :dictId")
    fun getDictById(dictId: Int): List<DictEntity>

    @Query("SELECT * FROM wordindict_table WHERE dictid = :dictId")
    fun getWordsInDict(dictId: Int): List<WordInDictEntity>

    @Query("""
        SELECT dict_table.id AS id, 
        dict_table.name AS name,
        dict_table.downloaded AS downloaded,
        dict_progress.total AS totalCount,
        dict_progress.repeat AS repeatCount,
        dict_progress.wait AS waitCount,
        dict_progress.done AS doneCount  
        FROM dict_table
        LEFT JOIN (
        SELECT wordindict_table.dictid,  
        SUM(CASE WHEN progress_table.status  = 12 THEN 1 ELSE 0 END) AS done, 
        SUM(CASE WHEN ifnull(progress_table.status,0) > 0 AND ifnull(progress_table.status,0) < 12 AND progress_table.repeatdate <= :curTimestamp THEN 1 ELSE 0 END) AS repeat,
        SUM(CASE WHEN ifnull(progress_table.status,0) > 0 AND ifnull(progress_table.status,0) < 12 AND progress_table.repeatdate > :curTimestamp THEN 1 ELSE 0 END) AS wait,
        COUNT(wordindict_table.wordid) AS total
        from wordindict_table 
        JOIN words_table ON words_table.id = wordindict_table.wordid AND NOT words_table.deleted
        LEFT JOIN progress_table ON progress_table.userid = :userId AND progress_table.wordid = wordindict_table.wordid
        GROUP BY wordindict_table.dictid
        ) AS dict_progress ON dict_progress.dictid = dict_table.id
        """)
    fun getDictData(userId: Int, curTimestamp: Long): List<DictData>

    @Query("""
        SELECT wordindict_table.wordid AS wordid,
        words_table.word AS word,
        IFNULL(progress_table.repeatdate,0) AS repeatdate,
        IFNULL(progress_table.status, 0)  AS status
        FROM wordindict_table
        JOIN words_table ON words_table.id = wordindict_table.wordid AND NOT words_table.deleted
        LEFT JOIN progress_table ON progress_table.wordid = wordindict_table.wordid AND progress_table.userid = :userId
        WHERE dictid = :dictId
        ORDER BY word
        """)
    fun getWords(dictId: Int, userId: Int): List<DraftWordData>

    @Query("""
        SELECT 
            word_translate2.wordid AS wordid,
            word_translate2.word AS word,
            transtale_result1.translate AS translate1,
            transtale_result2.translate AS translate2,
            IFNULL(progress_table.repeatdate,0) AS repeatdate,
            IFNULL(progress_table.status, 0)  AS status
        FROM ( 
            SELECT 
                word_translate1.wordid,
                word_translate1.word,
                word_translate1.translate_id1,
                MIN(wordtranslate_table.id) AS translate_id2
            FROM ( 
                SELECT 
                    words_table.id AS wordid,
                    words_table.word AS word,
                    MIN(wordtranslate_table.id) AS translate_id1
                FROM words_table
                JOIN wordtranslate_table 
                    ON wordtranslate_table.wordid = words_table.id
                WHERE NOT words_table.deleted
                GROUP BY  words_table.id, words_table.word 
            ) word_translate1
            LEFT JOIN wordtranslate_table 
                ON wordtranslate_table.wordid = word_translate1.wordid 
                AND wordtranslate_table.id > word_translate1.translate_id1
            GROUP BY  word_translate1.wordid, word_translate1.word, word_translate1.translate_id1
        ) AS word_translate2
        JOIN wordtranslate_table transtale_result1 
            ON transtale_result1.id = word_translate2.translate_id1
        JOIN wordtranslate_table transtale_result2 
            ON transtale_result2.id = word_translate2.translate_id2
        LEFT JOIN progress_table 
            ON progress_table.wordid = word_translate2.wordid 
            AND progress_table.userid = :userId
        ORDER BY word_translate2.word
        """)
    fun getAllWords(userId: Int): List<WordAndTranslatesData>

    @Query("""
        SELECT wordindict.wordid wordid, wordtranslate.id id, wordtranslate.translate translate, COALESCE(prioritytranslate.count, 0) priority 
        FROM wordindict_table wordindict
        INNER JOIN wordtranslate_table wordtranslate ON wordtranslate.wordid = wordindict.wordid 
        LEFT JOIN priority_translate_table prioritytranslate 
        ON wordtranslate.id = prioritytranslate.translateid 
        AND prioritytranslate.dictid = :dictId
        WHERE wordindict.dictid = :dictId
    """)
    fun getTranslates(dictId: Int): List<TranslateData>

}

