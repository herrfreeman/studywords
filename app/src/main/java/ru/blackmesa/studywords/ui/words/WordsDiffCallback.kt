package ru.blackmesa.studywords.ui.words

import androidx.recyclerview.widget.DiffUtil
import ru.blackmesa.studywords.data.models.WordData

class WordsDiffCallback() : DiffUtil.Callback() {

    private val oldList = emptyList<WordData>().toMutableList()
    private val newList = emptyList<WordData>().toMutableList()

    fun setNewList(newList: List<WordData>) {
        this.oldList.clear()
        this.oldList.addAll(this.newList)
        this.newList.clear()
        this.newList.addAll(newList.map { it.copy() })
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].wordid == newList[newItemPosition].wordid
                && oldList[oldItemPosition].status == newList[newItemPosition].status
                && oldList[oldItemPosition].repeatdate == newList[newItemPosition].repeatdate)
    }
}

