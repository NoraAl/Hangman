package edu.umsl.hangman

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.Log.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.*


data class Line(var x1: Float, var y1: Float, var x2: Float, var y2: Float)

class HangmanCanvas : View, ValueAnimator.AnimatorUpdateListener {

    companion object {
        const val PROPERTY_X = "PROPERTY_X"
        const val PROPERTY_Y = "PROPERTY_Y"
        const val PROPERTY_ALPHA = "PROPERTY_ALPHA"
        const val size = 8// number of lines needed
    }
    
    private val paint = Paint()
    private val loosePaint = Paint()
    private val animationPaint = Paint()
    private val circlePaint = Paint()
    private var mistakeCount: Int = 0
    private var animatorSet: AnimatorSet? = null
    private var lines: ArrayList<Line>
    private var animationLine: Line
    private var animationRect: Rect
    private var currentIndex = 0
    private var loosed = false;
    var animating = false


    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density * 4
        paint.color = Color.parseColor("#b30059")

        animationPaint.isAntiAlias = true
        animationPaint.strokeWidth = resources.displayMetrics.density * 5
        animationPaint.color = Color.parseColor("#ff6699")

        loosePaint.color = Color.parseColor("#ff6699")
        loosePaint.style = Paint.Style.FILL_AND_STROKE


        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = resources.displayMetrics.density * 9
        circlePaint.color = Color.parseColor("#ffcc66")

        lines = ArrayList(0)
        animationLine = Line(0f, 0f, 0f, 0f)
        animationRect = Rect(5, 5, 10, 10)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val padding = height * 0.02f
        val min = padding
        val maxX = width - padding
        val maxY = height - padding
        val middleX = maxX * 0.45f
        val middleY = maxY * 0.45f
        animationRect = Rect(min.toInt(), min.toInt(), maxX.toInt(), maxY.toInt())


        lines.clear()
        for (i in 0..size) {
            lines.add(when (i) {
                0 -> Line(maxX, maxY, maxX, min)//right side
                1 -> Line(maxX, min, middleX, min) //top side
                2 -> Line(middleX, min, middleX, middleY) // middle side
                3 -> Line(middleX, middleY, maxX * 0.55f, maxY * 0.55f) // right arm
                4 -> Line(middleX, middleY, middleX, maxY * 0.7f) // body
                5 -> Line(middleX, middleY, maxX * 0.35f, maxY * 0.55f) // left arm
                6 -> Line(middleX, maxY * 0.7f, maxX * 0.35f, maxY * 0.85f) // left leg
                else -> Line(middleX, maxY * 0.7f, maxX * 0.55f, maxY * 0.85f) // right leg
            })
        }


    }

    fun anotherMistake() {
        if (animating)
            return
        mistakeCount++
        e("---","$mistakeCount out of $size")
         if (mistakeCount > size)//this should happen only once
            animateLoose()
        else
            animateHangman()
    }

    override fun onDraw(canvas: Canvas) {

        if (loosed)
            drawLoosing(canvas)
        else
            drawLines(canvas)
        super.onDraw(canvas)
    }

    private fun animateLoose() {
        loosed = true

        val alpha = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, 0,255)

        val animator = ValueAnimator()
        animator.setValues(alpha)
        animator.duration = 1000
        //animator.interpolator = AccelerateDecelerateInterpolator()
        animator.repeatMode = REVERSE
        animator.repeatCount = 1
        animator.addUpdateListener {
            onAnimationUpdate(it)
        }
        animator.start()

    }

    private fun animateHangman() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet()
        animatorSet?.playSequentially(getAnimators())
        animating = true
        animatorSet?.start()
    }

    private fun getAnimators(): List<Animator> {
        val animators = ArrayList<Animator>()
        for (i in 0 until mistakeCount) {
            animators.add(singleAnimator(lines[i], i))
        }
        return animators
    }

    //create each animator to values of related line
    private fun singleAnimator(line: Line, last: Int): ValueAnimator {
        val slow = (last == mistakeCount - 1) // last mistake line
        val x = PropertyValuesHolder.ofFloat(PROPERTY_X, line.x1, line.x2)
        val y = PropertyValuesHolder.ofFloat(PROPERTY_Y, line.y1, line.y2)

        val animator = ValueAnimator()
        animator.setValues(x, y)
        animator.duration = if (slow) 500 else 100
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            onAnimationUpdate(it)
        }
        return animator
    }

    private fun animationIndex(): Int {
        val childAnimations = animatorSet?.childAnimations ?: return -1
        for (i in childAnimations.indices) {
            val animator = childAnimations[i]
            if (animator.isRunning) {
                if (i > lines.size)
                    animator.end()
                return i
            }
        }
        return -1
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        animation ?: return
        if (loosed){
            val alpha = animation.getAnimatedValue(PROPERTY_ALPHA) as Int
            loosePaint.alpha = alpha

//            paint.style = Paint.Style.FILL
            invalidate()
            return
        }

        val x = animation.getAnimatedValue(PROPERTY_X) as Float
        val y = animation.getAnimatedValue(PROPERTY_Y) as Float
        currentIndex = animationIndex()
        if (currentIndex < 0 || currentIndex >= lines.size) {
            currentIndex = 0
            return
        }

        animationLine = Line(lines[currentIndex].x1, lines[currentIndex].y1, x, y)
        invalidate()

    }


    private fun drawLoosing(canvas: Canvas) {

        canvas.drawRect(animationRect,loosePaint)
        val textBounds = Rect()
        circlePaint.textSize = 80f
        val text = "Hard Luck!"
        circlePaint.getTextBounds(text, 0, text.length, textBounds);
        canvas.drawText(text, animationRect.exactCenterX()- textBounds.exactCenterX(), animationRect.exactCenterY() - textBounds.exactCenterY(), circlePaint);

    }




    private fun drawLines(canvas: Canvas) {
        if (currentIndex < 0 || currentIndex > lines.size)
            return


        //canvas.drawRect(animationRect,loosePaint)
        canvas.drawLine(animationLine.x1, animationLine.y1, animationLine.x2, animationLine.y2, paint)
        for (i in 0 until mistakeCount - 1) { // draw each proceeding line to the current
            canvas.drawLine(lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2, paint)
            canvas.drawCircle(lines[i].x2, lines[i].y2, 5f, paint) // circle is just to smooth edges
        }


        if (animationLine.x2 == lines[currentIndex].x2 && animationLine.y2 == lines[currentIndex].y2) {
            animating = false // open lock
            return
        }
        if (currentIndex == mistakeCount)
            return

        canvas.drawLine(animationLine.x1, animationLine.y1, animationLine.x2, animationLine.y2, animationPaint)
        canvas.drawCircle(animationLine.x2, animationLine.y2, 10f, circlePaint)
    }
}





