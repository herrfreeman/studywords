package ru.blackmesa.studywords.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.WordData

class WordsViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.words_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.wordCaption)
    private val itemStatus: TextView = itemView.findViewById(R.id.wordStatus)

    fun bind(model: WordData) {
        itemCaption.text = "${model.word} - ${model.translate}"
        itemStatus.text = "${model.newprogress}"
    }

}