package ru.blackmesa.studywords.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.DictData

class LibraryViewHolder(parentView: ViewGroup, private val itemClickListener: LibraryRVAdapter.ItemClickListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.library_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.lib_item_caption)
    private val wordsCount: TextView = itemView.findViewById(R.id.wordsCount)
    private val repeatCount: TextView = itemView.findViewById(R.id.repeatCount)
    private val statusView: View = itemView.findViewById(R.id.statusView)
    private val downloadButton: View = itemView.findViewById(R.id.download_button)

    fun bind(model: DictData) {

        if (model.totalCount > 0) {
            downloadButton.isVisible = false
            itemView.isEnabled = true
        } else {
            downloadButton.isVisible = true
            itemView.isEnabled = false
        }

        itemCaption.text = model.name
        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }
        downloadButton.setOnClickListener {
            itemClickListener.onDownloadClick(model)
        }

        wordsCount.text = "${model.totalCount}"
        if (model.repeatCount > 0) {
            repeatCount.text = " | ${model.repeatCount}"
        } else {
            repeatCount.text = ""
        }
        statusView.background = StatusBarDrawable(model.totalCount, model.doneCount, model.repeatCount, model.waitCount)

    }

}