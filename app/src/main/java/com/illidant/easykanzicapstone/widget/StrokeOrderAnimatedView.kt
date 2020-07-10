package com.illidant.easykanzicapstone.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.illidant.easykanzicapstone.R
import com.illidant.easykanzicapstone.util.SVGFileHelper
import java.io.File
import java.io.InputStream
import java.util.*

class StrokeOrderAnimatedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val rawPathList = mutableListOf<Path>()
    private val rawPathLengthList = mutableListOf<Float>()
    private val pathList = mutableListOf<Path>()
    private val pathMeasureList = mutableListOf<PathMeasure>()
    private val transformMatrix = Matrix()

    private var drawAnimation: ValueAnimator? = null
    private var dashPathEffect: DashPathEffect? = null
    private var strokeIndex = 0
    private val fingerPosition = PointF(0f, 0f)
    private val pos = FloatArray(2)

    private val animStarted: Boolean
        get() = drawAnimation?.isStarted ?: false
    private val animRunning: Boolean
        get() = drawAnimation?.isRunning ?: false
    private val strokeCount: Int
        get() = pathList.size
    private val isKanjiDrawn: Boolean
        get() = strokeIndex == strokeCount

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val typedArray = context.obtainStyledAttributes(
        attrs,
        R.styleable.StrokeOrderAnimatedView,
        defStyleAttr,
        0
    )
    private val animPaint = Paint(paint)
    private val fingerPaint = Paint(paint).apply { style = Paint.Style.FILL }
    private val lightPaint = Paint(paint)
    private val highlightedStrokeList = TreeSet<Int>()

    private var autoRun = false
    private var autoRunDelay = 500L
    private var animate = true
    private var strokeColor = Color.BLACK
    private var highlightColor = Color.RED
    private var drawLight = true
        set(value) {
            field = value
            invalidate()
        }

    init {
        val primaryColor = TypedValue().let {
            context.theme.resolveAttribute(R.attr.colorPrimary, it, true)
            it.data
        }
        val accentColor = TypedValue().let {
            context.theme.resolveAttribute(R.attr.colorAccent, it, true)
            it.data
        }

        if (attrs != null) {
            autoRun = typedArray.getBoolean(R.styleable.StrokeOrderAnimatedView_autoRun, false)
            animate = typedArray.getBoolean(R.styleable.StrokeOrderAnimatedView_animate, true)
            strokeColor =
                typedArray.getColor(R.styleable.StrokeOrderAnimatedView_strokeColor, Color.BLACK)
            highlightColor = typedArray.getColor(
                R.styleable.StrokeOrderAnimatedView_strokeHighlightColor,
                accentColor
            )
            fingerPaint.color =
                typedArray.getColor(R.styleable.StrokeOrderAnimatedView_fingerColor, primaryColor)
            lightPaint.color = typedArray.getColor(
                R.styleable.StrokeOrderAnimatedView_strokeLightColor,
                Color.argb(50, 0, 0, 0)
            )
            fingerPaint.strokeWidth = typedArray.getDimension(
                R.styleable.StrokeOrderAnimatedView_fingerRadius,
                resources.getDimension(R.dimen.dp_8)
            )
            typedArray.getDimension(
                R.styleable.StrokeOrderAnimatedView_strokeWidth,
                resources.getDimension(R.dimen.dp_8)
            ).let {
                paint.strokeWidth = it
                animPaint.strokeWidth = it
                lightPaint.strokeWidth = it
            }
        }

        animPaint.strokeWidth = paint.strokeWidth

        if (isInEditMode) {
            loadPathData(previewStrokePathData)
            strokeIndex = 1
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        transformMatrix.setRectToRect(
            inputRect,
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            Matrix.ScaleToFit.FILL
        )

        applyTransformMatrix()

        if (isInEditMode) {
            val length = pathMeasureList[strokeIndex].length
            dashPathEffect = DashPathEffect(floatArrayOf(length, length), length * 0.6f)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (strokeCount == 0) return

        if (!animate) {
            for (i in 0 until strokeCount) {
                paint.color = if (highlightedStrokeList.contains(i)) highlightColor else strokeColor
                canvas.drawPath(pathList[i], paint)
            }
            return
        }

        if (!isKanjiDrawn && !animStarted && autoRun) {
            startDrawAnimation(autoRunDelay)
        }

        if (drawLight) {
            for (i in 0 until strokeCount) {
                canvas.drawPath(pathList[i], lightPaint)
            }
        }

        for (i in 0 until strokeIndex) {
            paint.color = if (highlightedStrokeList.contains(i)) highlightColor else strokeColor
            canvas.drawPath(pathList[i], paint)
        }

        if (dashPathEffect != null && (animRunning || isInEditMode)) {
            animPaint.pathEffect = dashPathEffect
            animPaint.color =
                if (highlightedStrokeList.contains(strokeIndex + 1)) highlightColor else strokeColor
            canvas.drawPath(pathList[strokeIndex], animPaint)
            canvas.drawCircle(
                fingerPosition.x,
                fingerPosition.y,
                fingerPaint.strokeWidth,
                fingerPaint
            )
        }
    }

    fun setPathData(view: StrokeOrderAnimatedView) {
        clear()

        rawPathList.addAll(view.rawPathList)
        rawPathLengthList.addAll(view.rawPathLengthList)

        applyTransformMatrix()
        invalidate()
    }

    fun loadPathData(pathDataList: List<String>) = buildPathList(pathDataList)

    fun loadSVGFile(url: String) = buildPathList(SVGFileHelper.extractPathData(url))

    fun loadSVGFile(stream: InputStream) =
        loadSVGFile(stream.bufferedReader().use { it.readText() })

    fun loadSVGFile(file: File) = loadSVGFile(file.readText())

    @JvmOverloads
    fun startDrawAnimation(delay: Long = 0) {
        if (animStarted) {
            drawAnimation?.cancel()
            drawAnimation = null
        }

        strokeIndex = 0

        if (strokeCount > 0) {
            startStrokeAnimation(delay)
        }
    }

    fun highlightStroke(stroke: Int) {
        if (stroke in 0 until (strokeCount - 1) && highlightedStrokeList.add(stroke)) {
            invalidate()
        }
    }

    fun highlightStrokes(strokeList: List<Int>) {
        strokeList.filterTo(highlightedStrokeList) { it in 0 until (strokeCount - 1) }
        invalidate()
    }

    fun clearHighlightStrokList() {
        highlightedStrokeList.clear()
        invalidate()
    }

    private fun startStrokeAnimation(delay: Long) {
        val length = pathMeasureList[strokeIndex].length
        drawAnimation = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener { animation ->
                if (drawAnimation != this || strokeIndex >= strokeCount) {
                    cancel()
                    return@addUpdateListener
                }

                dashPathEffect =
                    DashPathEffect(
                        floatArrayOf(length, length),
                        length * (1f - animation.animatedFraction)
                    )
                pathMeasureList[strokeIndex].getPosTan(
                    length * animation.animatedFraction,
                    pos,
                    null
                )
                fingerPosition.set(pos[0], pos[1])

                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                var cancelled = false

                override fun onAnimationCancel(animation: Animator?) {
                    cancelled = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (cancelled) return

                    if (++strokeIndex < strokeCount) {
                        startStrokeAnimation(0)
                    } else {
                        this@StrokeOrderAnimatedView.drawAnimation = null
                        dashPathEffect = null
                    }
                }
            })

            duration = (rawPathLengthList[strokeIndex] * 10).toLong()
        }

        drawAnimation?.startDelay = delay
        drawAnimation?.start()
    }

    private fun applyTransformMatrix() {
        if (pathList.isEmpty()) {
            pathList.addAll(List(rawPathList.size) { Path() })
        }
        rawPathList.forEachIndexed { i, path ->
            path.transform(transformMatrix, pathList[i])
        }
        pathMeasureList.clear()
        pathList.mapTo(pathMeasureList) { PathMeasure(it, false) }
    }

    private fun buildPathList(strokePathData: List<String>?) {
        clear()
        if (strokePathData.orEmpty().isNotEmpty()) {
            strokePathData?.mapTo(rawPathList) { SVGFileHelper.buildPathData(it) }
            rawPathList.mapTo(rawPathLengthList) { PathMeasure(it, false).length }
            applyTransformMatrix()
        }
        invalidate()
    }

    private fun clear() {
        rawPathList.clear()
        rawPathLengthList.clear()
        highlightedStrokeList.clear()
        pathList.clear()
        pathMeasureList.clear()
        strokeIndex = 0
        drawAnimation?.cancel()
    }

    companion object {
        private val inputRect = RectF(0f, 0f, 109f, 109f)
        private val previewStrokePathData = listOf(
            "M34.25,16.25c1,1,1.48,2.38,1.5,4c0.38,33.62,2.38,59.38-11,73.25",
            "M36.25,19c4.12-0.62,31.49-4.78,33.25-5c4-0.5,5.5,1.12,5.5,4.75c0,2.76-0.5,49.25-0.5,69.5c0,13-6.25,4-8.75,1.75",
            "M37.25,38c10.25-1.5,27.25-3.75,36.25-4.5",
            "M37,58.25c8.75-1.12,27-3.5,36.25-4"
        )
    }
}
