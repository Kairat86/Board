package zhet.board.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawingView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private var mPaint: Paint = Paint()
    private var mPath: Path = Path()
    private val map = HashMap<Path, Paint>()

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 2F
        map[mPath] = mPaint
    }

    override fun onDraw(canvas: Canvas) {
        map.forEach {
            canvas.drawPath(it.key, it.value)
        }
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN -> mPath.moveTo(event.x, event.y)

            MotionEvent.ACTION_MOVE -> {
                mPath.lineTo(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
            }
        }

        return true
    }

    fun setColor(color: Int) {
        val tmpStroke = mPaint.strokeWidth
        mPaint = Paint()
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = tmpStroke
        mPath = Path()
        map[mPath] = mPaint
    }

    fun setSize(value: Float) {
        val tmpColor = mPaint.color
        mPaint = Paint()
        mPaint.color = tmpColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = value
        mPath = Path()
        map[mPath] = mPaint
    }

    fun getSize(): Float {
        return mPaint.strokeWidth
    }

    fun getColor(): Int {
        return mPaint.color
    }
}