package ru.blackmesa.studywords.ui.library

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.data.models.Dictionary


class LibraryRVAdapter : RecyclerView.Adapter<LibraryViewHolder>() {

    val library: MutableList<Dictionary> = emptyList<Dictionary>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder =
        LibraryViewHolder(parent)

    override fun getItemCount(): Int = library.count()

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(library[position])
    }

}