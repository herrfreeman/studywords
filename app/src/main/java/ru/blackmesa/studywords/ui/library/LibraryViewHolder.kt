package ru.blackmesa.studywords.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.Dictionary

class LibraryViewHolder(parentView: ViewGroup, private val itemClickListener: LibraryRVAdapter.ItemClickListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.library_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.libItemCaption)

    fun bind(model: Dictionary) {
        itemCaption.text = model.name
        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }
    }

}