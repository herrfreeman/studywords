package ru.blackmesa.studywords.ui.library

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.data.models.Dictionary


class LibraryRVAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<LibraryViewHolder>() {

    val library: MutableList<Dictionary> = emptyList<Dictionary>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder =
        LibraryViewHolder(parent, itemClickListener)

    override fun getItemCount(): Int = library.count()

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(library[position])
    }

    fun interface ItemClickListener {
        fun onItemClick(item: Dictionary)
    }

}