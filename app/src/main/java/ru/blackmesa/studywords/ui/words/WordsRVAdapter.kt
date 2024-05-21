package ru.blackmesa.studywords.ui.words

import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordWithTranslate


class WordsRVAdapter : RecyclerView.Adapter<WordsViewHolder>() {

    val words: MutableList<WordWithTranslate> = emptyList<WordWithTranslate>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder =
        WordsViewHolder(parent)

    override fun getItemCount(): Int = words.count()

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(words[position])
    }

}