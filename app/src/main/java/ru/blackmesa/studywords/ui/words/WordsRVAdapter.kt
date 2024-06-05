package ru.blackmesa.studywords.ui.words

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.ui.library.LibraryRVAdapter


class WordsRVAdapter(private val itemClickListener: WordsRVAdapter.ItemClickListener) : RecyclerView.Adapter<WordsViewHolder>() {

    val words: MutableList<WordData> = emptyList<WordData>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder =
        WordsViewHolder(parent, itemClickListener)

    override fun getItemCount(): Int = words.count()

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(words[position])
    }

    fun interface ItemClickListener {
        fun onItemClick(item: WordData)
    }
}