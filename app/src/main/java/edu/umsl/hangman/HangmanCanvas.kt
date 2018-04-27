package edu.umsl.hangman

import android.animation.Animator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.ArrayList
import android.animation.AnimatorSet
import android.util.Log


data class Line(var x1: Float, var y1: Float, var x2: Float, var y2: Float)

class HangmanCanvas : View, ValueAnimator.AnimatorUpdateListener {

    companion object {
        const val PROPERTY_X = "PROPERTY_X"
        const val PROPERTY_Y = "PROPERTY_Y"
    }

    private var size = 0
    private val paint = Paint()
    private val animationPaint = Paint()
    private val circlePaint = Paint()
    private var mistakeCount: Int = 0
    private var animatorSet: AnimatorSet? = null
    private var lines: ArrayList<Line>
    private var animationLine: Line
    private var currentIndex = 0

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    init {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density * 4
        paint.color = Color.parseColor("#b30059")

        animationPaint.isAntiAlias = true
        animationPaint.strokeWidth = resources.displayMetrics.density * 5
        animationPaint.color = Color.parseColor("#cc0066")

        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = resources.displayMetrics.density * 9
        circlePaint.color = Color.parseColor("#ff6600")

        lines = ArrayList(0)
        animationLine = Line(0f,0f,0f,0f)
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

        size = 8
        lines.clear()
        for (i in 0..size){
            lines.add(when(i){
                0 -> Line(maxX, maxY, maxX, min)//right side
                1 ->  Line(maxX, min, middleX, min) //top side
                2 -> Line(middleX, min, middleX, middleY) // middle side
                3 -> Line(middleX, middleY, maxX*0.55f, maxY*0.55f) // right arm
                4 -> Line(middleX, middleY, middleX, maxY*0.7f) // body
                5 -> Line(middleX, middleY, maxX*0.35f, maxY*0.55f) // left arm
                6 -> Line(middleX, maxY*0.7f, maxX*0.35f, maxY*0.85f) // left leg
                else -> Line(middleX, maxY*0.7f, maxX*0.55f, maxY*0.85f) // right leg
            })
        }
    }

    fun anotherMistake() {
        //currentIndex = 0
        mistakeCount++
        animateHangman()
    }

    override fun onDraw(canvas: Canvas) {
        drawLines(canvas)
        super.onDraw(canvas)
        return
    }

    private fun animateHangman() {
        animatorSet?.cancel()
        animatorSet = AnimatorSet()
        animatorSet?.playSequentially(getAnimators())
        animatorSet?.start()
    }

    private fun getAnimators(): List<Animator> {
        val animators = ArrayList<Animator>()
        for (line in lines) {
            animators.add(singleAnimator(line))
        }
        return animators
    }

    //create each animator to values of related line
    private fun singleAnimator(line: Line): ValueAnimator {
        val x = PropertyValuesHolder.ofFloat(PROPERTY_X, line.x1, line.x2)
        val y = PropertyValuesHolder.ofFloat(PROPERTY_Y, line.y1, line.y2)

        val animator = ValueAnimator()
        animator.setValues(x, y)
        animator.duration = 1000
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
                if (i>lines.size)
                    animator.end()
                return i
            }
        }
        return -1
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        animation ?: return

        val x = animation.getAnimatedValue(PROPERTY_X) as Float
        val y = animation.getAnimatedValue(PROPERTY_Y) as Float
        currentIndex = animationIndex()
        animationLine = Line(lines[currentIndex].x1, lines[currentIndex].y1, x, y)
        invalidate()
        if (currentIndex<0 || currentIndex>=lines.size) {
            println("out of boundaries $currentIndex")
            currentIndex = 0
            return
        }
    }


    private fun drawLines(canvas: Canvas) {
        if (currentIndex < 0 || currentIndex > lines.size)
            return

        for (i in 0 until currentIndex) {
            // draw each proceeding line to the current
            canvas.drawLine(lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2, paint)
            canvas.drawCircle(lines[i].x2, lines[i].y2,5f,paint)

        }

        canvas.drawLine(animationLine.x1,animationLine.y1, animationLine.x2, animationLine.y2, animationPaint)
        canvas.drawCircle(animationLine.x2,animationLine.y2,8f,circlePaint)
    }
}





