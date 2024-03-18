package com.example.vkwatch

import android.graphics.Color
import android.graphics.Paint

object CONSTANTS{
    var mDialColor: Int = Color.GRAY
    var mPointsColor: Int = Color.WHITE
    var mHourHandColor: Int = Color.GREEN
    var mMinuteHandColor: Int = Color.YELLOW
    var mSecondHandColor: Int = Color.RED
    var mHourHandWidth: Float = 15f
    var mMinuteHandWidth: Float = 12f
    var mSecondHandWidth: Float = 8f

    // paints
    var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    lateinit var mDialPaint: Paint
    lateinit var mPointerPaint: Paint

    // custom view or canvas width, height and clock radius
    var mWidth = 0.0f
    var mHeight = 0.0f
    var mRadius = 0.0f

    // initial position of clock
    var hourData = 0f
    var minuteData = 0f
    var secondData = 0
}