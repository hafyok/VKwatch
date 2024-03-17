package com.example.vkwatch

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.min


class AnalogClockView : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        setupAttributes(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        setupAttributes(attrs)
    }

    // custom attribute
    private var mDialColor: Int = Color.GRAY
    private var mPointsColor: Int = Color.WHITE
    private var mHourHandColor: Int = Color.GREEN
    private var mMinuteHandColor: Int = Color.YELLOW
    private var mSecondHandColor: Int = Color.RED
    private var mHourHandWidth: Float = 15f
    private var mMinuteHandWidth: Float = 12f
    private var mSecondHandWidth: Float = 8f


    // paints
    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var mDialPaint: Paint
    private lateinit var mPointerPaint: Paint

    // custom view or canvas width, height and clock radius
    private var mWidth = 0.0f
    private var mHeight = 0.0f
    private var mRadius = 0.0f

    init {
        // create text paint
        mTextPaint.color = Color.BLACK
        mTextPaint.style = Paint.Style.FILL_AND_STROKE
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 40f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //create DialPaint
        mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mDialPaint.color = mDialColor

        //create PointerPaint
        mPointerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointerPaint.color = mPointsColor

        // Draw the dial
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint)
    }

    // when view created or device rotate this will called so we can get width and height of canvas
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = ((min(mWidth, mHeight)) * (0.8f) / 2f)
    }


    // setup custom attribute
    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray: TypedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.AnalogClockView, 0, 0)

        // Extract custom attributes into member variables
        mDialColor =
            typedArray.getColor(R.styleable.AnalogClockView_dialColor, mDialColor)
        mPointsColor =
            typedArray.getColor(R.styleable.AnalogClockView_pointsColor, mPointsColor)
        mHourHandColor =
            typedArray.getColor(R.styleable.AnalogClockView_hourHandColor, mHourHandColor)
        mMinuteHandColor =
            typedArray.getColor(R.styleable.AnalogClockView_minuteHandColor, mMinuteHandColor)
        mSecondHandColor =
            typedArray.getColor(R.styleable.AnalogClockView_secondHandColor, mSecondHandColor)
        mHourHandWidth =
            typedArray.getFloat(R.styleable.AnalogClockView_hourHandWidth, mHourHandWidth)
        mMinuteHandWidth =
            typedArray.getFloat(R.styleable.AnalogClockView_minuteHandWidth, mMinuteHandWidth)
        mSecondHandWidth =
            typedArray.getFloat(R.styleable.AnalogClockView_secondHandWidth, mSecondHandWidth)

        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }

}