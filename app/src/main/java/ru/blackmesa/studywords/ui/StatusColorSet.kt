package ru.blackmesa.studywords.ui

import android.content.Context
import ru.blackmesa.studywords.R

class StatusColorSet(context: Context) {
    val doneColor: Int = context.resources.getColor(R.color.status_done, null )
    val waitColor: Int = context.resources.getColor(R.color.status_wait, null )
    val repeatColor: Int = context.resources.getColor(R.color.status_repeat, null )
}
    
