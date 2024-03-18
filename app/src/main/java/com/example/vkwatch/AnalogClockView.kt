package com.example.vkwatch

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.vkwatch.CONSTANTS.hourData
import com.example.vkwatch.CONSTANTS.mDialPaint
import com.example.vkwatch.CONSTANTS.mHeight
import com.example.vkwatch.CONSTANTS.mHourHandColor
import com.example.vkwatch.CONSTANTS.mHourHandWidth
import com.example.vkwatch.CONSTANTS.mMinuteHandColor
import com.example.vkwatch.CONSTANTS.mMinuteHandWidth
import com.example.vkwatch.CONSTANTS.mRadius
import com.example.vkwatch.CONSTANTS.mSecondHandColor
import com.example.vkwatch.CONSTANTS.mSecondHandWidth
import com.example.vkwatch.CONSTANTS.mWidth
import com.example.vkwatch.CONSTANTS.minuteData
import com.example.vkwatch.CONSTANTS.secondData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


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

    // create handler and time updater Runnable
    private var mHandler: Handler = Handler()
    private var mTimeUpdater: Runnable

    init {
        CONSTANTS.apply {
            mTextPaint.color = Color.BLACK
            mTextPaint.style = Paint.Style.FILL_AND_STROKE
            mTextPaint.textAlign = Paint.Align.CENTER
            mTextPaint.textSize = 40f
        }

        // initialize time updater runnable
        mTimeUpdater = object : Runnable {
            override fun run() {
                // update clock time data
                val currentTime: String =
                    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                setClockTime(
                    currentTime.substring(0, 2).toFloat() % 12,
                    currentTime.substring(3, 5).toFloat(),
                    currentTime.substring(6, 8).toInt()
                )
                // recall update runnable after 1 sec (1000ms)
                mHandler.postDelayed(this, 1000)
            }
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        CONSTANTS.apply {
            mDialPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mDialPaint.color = mDialColor

            //create PointerPaint
            mPointerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPointerPaint.color = mPointsColor
        }


        // Draw the dial
        canvas.drawCircle(
            mWidth / 2,
            mHeight / 2,
            mRadius,
            mDialPaint
        )

        // Draw the indicator mark.
        val numberCircleRadius = mRadius - 60
        CONSTANTS.apply {
            mTextPaint.style = Paint.Style.FILL
            mTextPaint.color = Color.WHITE
            mTextPaint.textSize = 35f

            for (i in 0..11) {
                val xyData = calcXYForPosition(i.toFloat(), numberCircleRadius, 30)
                if (i == 0){
                    canvas.drawText("12", xyData[0], xyData[1], mTextPaint)
                }else {
                    canvas.drawText(i.toString(), xyData[0], xyData[1], mTextPaint)
                }
            }
        }

        // hour hand
        drawHandWithPaint(
            canvas,
            mHourHandColor,
            mHourHandWidth,
            calcXYForPosition(hourData, numberCircleRadius - 130, 30)
        )

        // minute hand
        drawHandWithPaint(
            canvas,
            mMinuteHandColor,
            mMinuteHandWidth,
            calcXYForPosition(minuteData, numberCircleRadius - 80, 6)
        )
        // second hand
        drawHandWithPaint(
            canvas,
            mSecondHandColor,
            mSecondHandWidth,
            calcXYForPosition(secondData.toFloat(), numberCircleRadius - 30, 6)
        )
    }

    private fun drawHandWithPaint(
        canvas: Canvas?,
        handColor: Int,
        strokeWidth: Float,
        xyData: ArrayList<Float>
    ) {
        val handPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        handPaint.color = handColor
        handPaint.strokeWidth = strokeWidth
        canvas?.drawLine(
            mWidth / 2,
            mHeight / 2,
            xyData[0],
            xyData[1],
            handPaint
        )
    }

    private fun calcXYForPosition(pos: Float, rad: Float, skipAngle: Int): ArrayList<Float> {
        val result = ArrayList<Float>(2)
        val startAngle = 270f

        // Angle between continually number radians. 360/12 = 30
        val angle = startAngle + (pos * skipAngle)

        result.add(0, (rad * cos(angle * Math.PI / 180) + width / 2).toFloat())
        result.add(1, (height / 2 + rad * sin(angle * Math.PI / 180)).toFloat())
        return result
    }


    // when view created or device rotate this will called so we can get width and height of canvas
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        CONSTANTS.apply {
            mWidth = w.toFloat()
            mHeight = h.toFloat()
            mRadius = ((min(mWidth, mHeight)) * (0.8f) / 2f)
        }
    }


    // setup custom attribute
    private fun setupAttributes(attrs: AttributeSet?) {
        // Obtain a typed array of attributes
        val typedArray: TypedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.AnalogClockView, 0, 0)

        // Extract custom attributes into member variables
        CONSTANTS.apply {
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
        }
        // TypedArray objects are shared and must be recycled.
        typedArray.recycle()
    }

    // set clock hand
    private fun setClockTime(hour: Float, minute: Float, second: Int) {
        hourData = hour + (minute / 60)
        Log.d("time_data", hourData.toString())
        minuteData = minute
        secondData = second
        invalidate()
    }

    // start clock
    fun startClock() {
        mHandler.post(mTimeUpdater)
    }

    // stop clock
    fun stopClock() {
        mHandler.removeCallbacks(mTimeUpdater)
    }
}