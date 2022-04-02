package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var buttonLoadingWidth = 0f
    private var circleLoadingAngle = 0f
    private var buttonText = ""
    private val bound = Rect()

    private var loadingAnimation = ValueAnimator()
    private var circleAnimation = ValueAnimator()


    private val paintButton = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    private val paintLoadingButton = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }

    private val paintLoadingCircle = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.colorAccent)
    }

    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = Color.WHITE
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed)
    { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                buttonText = resources.getString(R.string.button_loading)
                startButtonAnimation()
                startCircleAnimation()
            }
            ButtonState.Completed -> {
                finishButtonAnimation()
                finishCircleAnimation()
                invalidate()
            }
        }
    }

    private fun startButtonAnimation() {
        loadingAnimation = ValueAnimator.ofFloat(0f, widthSize.toFloat()).apply {
            duration = 10000
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                buttonLoadingWidth = it.animatedValue as Float
                invalidate()
            }
        }
        loadingAnimation.start()
    }

    private fun startCircleAnimation() {
        circleAnimation = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 10000
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                circleLoadingAngle = it.animatedValue as Float
                invalidate()
            }
        }
        circleAnimation.start()
    }

    private fun finishButtonAnimation() {
        val startValue = loadingAnimation.animatedValue
        loadingAnimation.end()
        loadingAnimation = ValueAnimator.ofFloat(
            startValue as Float,
            widthSize.toFloat()
        ).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                buttonLoadingWidth = it.animatedValue as Float
                if (loadingAnimation.animatedFraction == 1f) {
                    buttonText = resources.getString(R.string.button_download)
                    buttonLoadingWidth = 0f
                }
                invalidate()
            }
        }
        loadingAnimation.start()
    }

    private fun finishCircleAnimation() {
        val startValue = loadingAnimation.animatedValue
        circleAnimation.end()
        circleAnimation = ValueAnimator.ofFloat(
            startValue as Float,
            360f
        ).apply {
            duration = 1000
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                circleLoadingAngle = it.animatedValue as Float
                if (circleAnimation.animatedFraction == 1f) {
                    circleLoadingAngle = 0f
                }
                invalidate()
            }
        }
        circleAnimation.start()
    }

    init {
        buttonText = resources.getString(R.string.button_download)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paintText.getTextBounds(buttonText, 0, buttonText.length, bound)
        canvas.drawPaint(paintButton)
        drawLoadingRectangle(canvas)
        drawLoadingCircle(canvas)
        drawText(canvas)
    }

    private fun drawLoadingRectangle(canvas: Canvas) {
        canvas.drawRect(0f, 0f, buttonLoadingWidth, canvas.height.toFloat(), paintLoadingButton)
    }

    private fun drawLoadingCircle(canvas: Canvas) {
        var left = (width / 2) + (bound.width() / 2)
        var top = (height / 2) - (bound.height() / 2)
        var right = (width / 2) + (bound.width() / 2) + (bound.height())
        var bottom = (height / 2) - (bound.height() / 2) + bound.height()
        canvas.drawArc(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            0f,
            circleLoadingAngle,
            true,
            paintLoadingCircle
        )
    }

    private fun drawText(canvas: Canvas) {
        val x = width.toFloat() / 2
        val y = height / 2f + bound.height() / 2f - bound.bottom
        canvas.drawText(buttonText, x, y, paintText)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0)
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}