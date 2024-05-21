package ru.blackmesa.studywords.ui.words

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.Dictionary
import ru.blackmesa.studywords.data.models.Word
import ru.blackmesa.studywords.data.models.WordWithTranslate

class WordsViewHolder(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.words_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.wordCaption)

    fun bind(model: WordWithTranslate) {
        itemCaption.text = "${model.word} - ${model.translate}"
    }

}