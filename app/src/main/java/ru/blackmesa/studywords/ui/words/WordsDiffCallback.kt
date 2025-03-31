package ru.blackmesa.studywords.ui.words

import androidx.recyclerview.widget.DiffUtil
import ru.blackmesa.studywords.data.models.WordData

class WordsDiffCallback() : DiffUtil.Callback() {

    private var oldList: List<WordData> = emptyList()
    private var newList: List<WordData> = emptyList()

    fun setNewList(newList: List<WordData>) {
        this.oldList = this.newList
        this.newList = newList
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].wordid == newList[newItemPosition].wordid
                && oldList[oldItemPosition].status == oldList[oldItemPosition].status
                && oldList[oldItemPosition].repeatdate == oldList[oldItemPosition].repeatdate)
    }
}

