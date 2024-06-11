package ru.blackmesa.studywords.ui.library

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class StatusBarDrawable(
    private val total: Int,
    private val done: Int,
    private val repeat: Int,
    private val wait: Int,
) : Drawable() {

    private val redPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#f9627d")
        strokeWidth = 10f
        style = Paint.Style.FILL
    }

    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        //color = Color.GREEN
        color = Color.parseColor("#83b692")
        strokeWidth = 10f
        //style = Paint.Style.STROKE
        style = Paint.Style.FILL
    }

    private val yellowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#fbb13c")
        strokeWidth = 10f
        //style = Paint.Style.STROKE
        style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        //canvas.drawRect(bounds, redPaint)

        if (total == 0) return

        val doneRight = bounds.right * done / total
        val repeatRight = bounds.right * repeat / total + doneRight
        val waitRight = bounds.right * wait / total + repeatRight




        canvas.drawRect(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            doneRight.toFloat(),
            bounds.bottom.toFloat(),
            greenPaint
        )

        canvas.drawRect(
            doneRight.toFloat(),
            bounds.top.toFloat(),
            repeatRight.toFloat(),
            bounds.bottom.toFloat(),
            redPaint
        )

        canvas.drawRect(
            repeatRight.toFloat(),
            bounds.top.toFloat(),
            waitRight.toFloat(),
            bounds.bottom.toFloat(),
            yellowPaint
        )

//        canvas.drawLine(
//            bounds.left.toFloat(),
//            bounds.top.toFloat(),
//            bounds.right.toFloat(),
//            bounds.bottom.toFloat(),
//            paint
//        )
    }

    override fun setAlpha(alpha: Int) {
        redPaint.alpha = alpha
        greenPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        redPaint.colorFilter = colorFilter
        greenPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}