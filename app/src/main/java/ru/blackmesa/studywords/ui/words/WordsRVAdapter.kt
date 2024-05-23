package ru.blackmesa.studywords.ui.words

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.data.models.WordData


class WordsRVAdapter : RecyclerView.Adapter<WordsViewHolder>() {

    val words: MutableList<WordData> = emptyList<WordData>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder =
        WordsViewHolder(parent)

    override fun getItemCount(): Int = words.count()

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(words[position])
    }

}