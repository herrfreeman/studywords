package ru.blackmesa.studywords.ui.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.data.models.DictData
import ru.blackmesa.studywords.data.models.Dictionary

class LibraryViewHolder(parentView: ViewGroup, private val itemClickListener: LibraryRVAdapter.ItemClickListener) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.library_item, parentView, false)
) {

    private val itemCaption: TextView = itemView.findViewById(R.id.libItemCaption)
    private val statusText: TextView = itemView.findViewById(R.id.statusText)


    private val statusDone: View = itemView.findViewById(R.id.statusDone)
    private val statusRepeat: View = itemView.findViewById(R.id.statusRepeat)
    private val statusWait: View = itemView.findViewById(R.id.statusWait)
    private val statusNew: View = itemView.findViewById(R.id.statusNew)

    fun bind(model: DictData) {

        itemCaption.text = model.name
        itemView.setOnClickListener {
            itemClickListener.onItemClick(model)
        }

        val total: Float = model.total.toFloat()
        var rest = total - model.done - model.repeat - model.wait

        statusText.text = "${model.total} / ${model.done} / ${model.repeat} / ${model.wait}"


        (statusDone.getLayoutParams() as LinearLayout.LayoutParams).weight = model.done / total
        (statusRepeat.getLayoutParams() as LinearLayout.LayoutParams).weight = model.repeat / total
        (statusWait.getLayoutParams() as LinearLayout.LayoutParams).weight = model.wait / total
        (statusNew.getLayoutParams() as LinearLayout.LayoutParams).weight = rest / total

    }

}