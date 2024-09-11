package ru.blackmesa.studywords.ui.words

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.koin.java.KoinJavaComponent
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.WordData
import ru.blackmesa.studywords.ui.StatusColorSet

class WordsViewHolder(
    parentView: ViewGroup,
    private val itemClickListener: WordsRVAdapter.ItemClickListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.words_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.wordCaption)
    private val itemStatus: View = itemView.findViewById(R.id.wordStatus)
    private val statusColorSet: StatusColorSet = KoinJavaComponent.getKoin().get()

    fun bind(model: WordData) {
        val currentTimestamp = System.currentTimeMillis() / 1000

        if (model.status == 12) {
            itemStatus.setBackgroundColor(statusColorSet.doneColor)
            itemCaption.text = "${model.word} - ${model.translate}"
        } else if (model.status == 0) {
            itemStatus.setBackgroundColor(0x00FFFFFF.toInt())
            itemCaption.text = "${model.word} - ${model.translate}"
        } else if (model.repeatdate <= currentTimestamp) {
            itemStatus.setBackgroundColor(statusColorSet.repeatColor)
            itemCaption.text = "? - ${model.translate}"
        } else {
            itemStatus.setBackgroundColor(statusColorSet.waitColor)
            itemCaption.text = "? - ${model.translate}"
        }

        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }

    }

}