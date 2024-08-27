package ru.blackmesa.studywords.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.DictData

class LibraryViewHolder(parentView: ViewGroup, private val itemClickListener: LibraryRVAdapter.ItemClickListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.library_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.libItemCaption)
    private val wordsCount: TextView = itemView.findViewById(R.id.wordsCount)
    private val repeatCount: TextView = itemView.findViewById(R.id.repeatCount)
    private val statusView: View = itemView.findViewById(R.id.statusView)

    fun bind(model: DictData) {

        itemCaption.text = model.name
        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }

        //wordsCount.text = "${model.total} / ${model.done} / ${model.repeat} / ${model.wait}"
        wordsCount.text = "${model.totalCount}"
        if (model.repeatCount > 0) {
            repeatCount.text = " | ${model.repeatCount}"
        }
        statusView.background = StatusBarDrawable(model.totalCount, model.doneCount, model.repeatCount, model.waitCount)

    }

}