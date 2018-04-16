package edu.umsl.hangman

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.reflect.Array.getLength
import android.R.attr.path
import android.graphics.PathMeasure
import android.opengl.ETC1.getHeight
import android.animation.TimeInterpolator
import android.view.animation.AccelerateInterpolator


class HangmanCanva : View {

    constructor(ctx: Context) : super(ctx)

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val paint = Paint()
    private val textPaint = Paint()
    //private val currentLine = Paint()
    private val path = Path()
    private val currentPath = Path()

    var mistakeCount = 0


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    private fun init() {
        paint.color = Color.parseColor("#3F51B5")
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 5

        textPaint.color = paint.color
        textPaint.isAntiAlias = true
        //textPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 70

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("onDraw", "--update")
        if (mistakeCount > 0) {
            when (mistakeCount) {
                1 -> singleMistake()
                2->two()
                else -> more()
            }

            path.addPath(currentPath)
            canvas.drawPath(path, paint)
            Log.e("onDraw", "-------")
        }
    }


    fun reset() {
        currentPath.reset()
    }

    private fun more() {
        val xStart = extra+width.toFloat()/2
        val yStart =0+padding+extra
        val xEnd = extra+width.toFloat()/2
        val yEnd = height.toFloat()/4

        reset()
        currentPath.moveTo(xStart, yStart)
        currentPath.lineTo(xEnd, yEnd)

    }

    val padding  = 8f
    val extra = 8

    private fun singleMistake() {// vertical right line
        val xStart = width.toFloat() - padding
        val yStart = height.toFloat()-padding
        val xEnd = xStart
        val yEnd = 0+padding

        reset()
        currentPath.moveTo(xStart, yStart)
        currentPath.lineTo(xEnd, yEnd)


    }

    private fun two() {//top line
        val xStart = width.toFloat() - padding
        val yStart = 0+padding+extra
        val xEnd = 4+width.toFloat()/2
        val yEnd = padding+extra

        reset()
        currentPath.moveTo(xStart, yStart)
        currentPath.lineTo(xEnd, yEnd)


    }


    fun anotherMistake() {
        mistakeCount++

        invalidate()
    }

    private fun prepareObjectAnimator() {
        //float w = (float)playGround.getWidth();
        val h = height
        val propertyStart = 0f
        val propertyEnd = 1f
        val propertyName = "alpha"
        val objectAnimator = ObjectAnimator.ofFloat(this, propertyName, propertyStart, propertyEnd)
        objectAnimator.duration = 31000
        objectAnimator.repeatCount = 1
        //objectAnimator.repeatMode = ObjectAnimator.REVERSE
        //objectAnimator.interpolator = timeInterpolator
        objectAnimator.start()
    }
}



