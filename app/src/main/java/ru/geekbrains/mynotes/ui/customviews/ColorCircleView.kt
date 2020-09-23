package ru.geekbrains.mynotes.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ru.geekbrains.mynotes.R

class ColorCircleView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var center: Pair<Float, Float> = 0f to 0f

    var radius: Float = context.resources.getDimension(R.dimen.def_radius_dp)

    @ColorRes
    var fillColorRes: Int = R.color.color_white
        set(value) {
            field = value
            fillPaint.color = ContextCompat.getColor(context, value)
        }

    @ColorRes var strokeColorRes: Int = R.color.design_default_color_secondary
        set(value) {
            field = value
            strokePaint.color = ContextCompat.getColor(context, value)
        }

    var strokeWidth: Float = context.resources.getDimension(R.dimen.def_stroke_width_dp)
        set(value) {
            field = value
            strokePaint.strokeWidth = value
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorCircleView)

        val defRadiusPx = context.resources.getDimension(R.dimen.def_radius_dp)
        radius = a.getDimension(R.styleable.ColorCircleView_circleRadius, defRadiusPx)
        fillColorRes = a.getResourceId(
            R.styleable.ColorCircleView_fillColor, R.color.color_white)

        val defStrokeWidthPx = context.resources.getDimension(R.dimen.def_stroke_width_dp)
        strokeWidth = a.getDimension(
            R.styleable.ColorCircleView_strokeWidth, defStrokeWidthPx)

        strokeColorRes = a.getResourceId(
            R.styleable.ColorCircleView_strokeColor, R.color.design_default_color_secondary)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (radius * 2 + paddingTop + paddingBottom).toInt()
        val width = (radius * 2 + paddingStart + paddingEnd).toInt()

        setMeasuredDimension(width, height)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        center = measuredWidth/2f to measuredHeight/2f
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(center.first, center.second, radius, strokePaint)
        canvas.drawCircle(center.first, center.second, radius - strokeWidth, fillPaint)
    }
}