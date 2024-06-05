package ru.blackmesa.studywords.ui.words

import android.graphics.ColorSpace.Rgb
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.WordData

class WordsViewHolder(parentView: ViewGroup, private val itemClickListener: WordsRVAdapter.ItemClickListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.words_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.wordCaption)
    private val itemStatus: View = itemView.findViewById(R.id.wordStatus)

    fun bind(model: WordData) {
        val currentTimestamp = System.currentTimeMillis() / 1000



//        itemStatus.text = when(model.status) {
//            in 1..4 -> "I"
//            in 5..8 -> "II"
//            in 9..11 -> "III"
//            else -> ""
//        }

        if(model.status == 12) {
            itemStatus.setBackgroundColor(0xFF4CAF50.toInt())
            itemCaption.text = "${model.word} - ${model.translate}"
        } else if(model.status == 0) {
            itemStatus.setBackgroundColor(0x00FFFFFF.toInt())
            itemCaption.text = "${model.word} - ${model.translate}"
        } else if(model.repeatdate <= currentTimestamp) {
            itemStatus.setBackgroundColor(0xFFFF5722.toInt())
            itemCaption.text = "? - ${model.translate}"
        } else {
            itemStatus.setBackgroundColor(0xFFFFC107.toInt())
            itemCaption.text = "? - ${model.translate}"
        }

        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }

    }

}