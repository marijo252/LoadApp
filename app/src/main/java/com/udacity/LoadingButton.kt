package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private lateinit var frame: Rect
    private val valueAnimator = ValueAnimator()

    private val paintButton = Paint().apply{
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context,R.color.colorPrimary)
    }


    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = Color.BLACK
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed)
    { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                //start the loading animation
                //similarly handle the other 2 states as well
            }
        }
    }


    init {

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPaint(paintButton)
        drawText(canvas)

    }

    private fun drawText(canvas: Canvas) {
        val textBound = Rect()
        val text = resources.getString(R.string.button_download)
        paintText.getTextBounds(text, 0, text.length, textBound)
        val x = width.toFloat()/2
        val y = canvas.height/2f + textBound.height()/2f - textBound.bottom

//        when(ButtonState){
//
//
//        }
        canvas.drawText(text, x, y, paintText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec,0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}