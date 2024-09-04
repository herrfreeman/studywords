package ru.blackmesa.studywords.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.DraftWordData
import ru.blackmesa.studywords.data.models.TranslateData

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
    fun insertPriorityTranslate(entities: List<PriorityTranslateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProgress(entities: List<ProgressEntity>)

    @Query("SELECT version FROM dict_table ORDER BY version DESC LIMIT 1;")
    fun getDictVersion(): List<Long>

    @Query("SELECT version FROM progress_table WHERE userid = :userid AND touched = false ORDER BY version DESC LIMIT 1;")
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

    @Query("SELECT * FROM progress_table WHERE userid = :userid AND touched = true;")
    fun getLocalProgress(userid: Int): List<ProgressEntity>

    @Query("SELECT * FROM dict_table ORDER BY orderfield")
    fun getDict(): List<DictEntity>

    @Query("SELECT * FROM wordindict_table WHERE dictid = :dictId")
    fun getWordsInDict(dictId: Int): List<WordInDictEntity>

    @Query(
        """
        select dict_table.id as id, 
        dict_table.name as name, 
        dict_progress.total as totalCount,
        dict_progress.repeat as repeatCount,
        dict_progress.wait as waitCount,
        dict_progress.done as doneCount  
        from dict_table
        left join (
        select wordindict_table.dictid,  
        SUM(case when progress_table.status  = 12 then 1 else 0 end) as done, 
        SUM(case when ifnull(progress_table.status,0) > 0 and ifnull(progress_table.status,0) < 12 and progress_table.repeatdate <= :curTimestamp then 1 else 0 end) as repeat,
        SUM(case when ifnull(progress_table.status,0) > 0 and ifnull(progress_table.status,0) < 12 and progress_table.repeatdate > :curTimestamp then 1 else 0 end) as wait,
        COUNT(wordindict_table.wordid) as total
        from wordindict_table 
        left join progress_table on progress_table.userid = :userId and progress_table.wordid = wordindict_table.wordid
        group by wordindict_table.dictid
        ) as dict_progress on dict_progress.dictid = dict_table.id
        """
    )
    fun getDictData(userId: Int, curTimestamp: Long): List<DictData>

    @Query(
        """SELECT wordindict_table.wordid AS wordid,
        words_table.word AS word,
        IFNULL(progress_table.repeatdate,0) AS repeatdate,
        IFNULL(progress_table.status, 0)  AS status
        FROM wordindict_table
        JOIN words_table ON words_table.id = wordindict_table.wordid
        LEFT JOIN progress_table ON progress_table.wordid = wordindict_table.wordid AND progress_table.userid = :userId
        WHERE dictid = :dictId ORDER BY word"""
    )
    fun getWords(dictId: Int, userId: Int): List<DraftWordData>

    @Query(
        """
    SELECT wordindict.wordid wordid, wordtranslate.id id, wordtranslate.translate translate, COALESCE(prioritytranslate.count, 0) priority 
    FROM wordindict_table wordindict
    INNER JOIN wordtranslate_table wordtranslate ON wordtranslate.wordid = wordindict.wordid 
    LEFT JOIN priority_translate_table prioritytranslate 
    ON wordtranslate.id = prioritytranslate.translateid 
    AND prioritytranslate.dictid = :dictId
    WHERE wordindict.dictid = :dictId
    """
    )
    fun getTranslates(dictId: Int): List<TranslateData>


}

