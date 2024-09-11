package ru.blackmesa.studywords.ui.library

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import org.koin.java.KoinJavaComponent.getKoin
import ru.blackmesa.studywords.R
import ru.blackmesa.studywords.ui.StatusColorSet

class StatusBarDrawable(
    private val total: Int,
    private val done: Int,
    private val repeat: Int,
    private val wait: Int,
) : Drawable() {

    private val statusColorSet: StatusColorSet = getKoin().get()

    private val repeatPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //color = Color.parseColor("#f9627d")
        color = statusColorSet.repeatColor
        strokeWidth = 10f
        style = Paint.Style.FILL
    }

    private val donePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //color = Color.parseColor("#83b692")
        color = statusColorSet.doneColor
        strokeWidth = 10f
        style = Paint.Style.FILL
    }

    private val waitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //color = Color.parseColor("#fbb13c")
        color = statusColorSet.waitColor
        strokeWidth = 10f
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (total == 0) return
        val doneRight = bounds.right * done / total
        val repeatRight = bounds.right * repeat / total + doneRight
        val waitRight = bounds.right * wait / total + repeatRight

        canvas.drawRect(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            doneRight.toFloat(),
            bounds.bottom.toFloat(),
            donePaint
        )

        canvas.drawRect(
            doneRight.toFloat(),
            bounds.top.toFloat(),
            repeatRight.toFloat(),
            bounds.bottom.toFloat(),
            repeatPaint
        )

        canvas.drawRect(
            repeatRight.toFloat(),
            bounds.top.toFloat(),
            waitRight.toFloat(),
            bounds.bottom.toFloat(),
            waitPaint
        )

    }

    override fun setAlpha(alpha: Int) {
        repeatPaint.alpha = alpha
        donePaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        repeatPaint.colorFilter = colorFilter
        donePaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}