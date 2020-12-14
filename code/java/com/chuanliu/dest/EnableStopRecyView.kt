package com.chuanliu.dest

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class EnableStopRecyView : RecyclerView{
    constructor(context: Context):super(context)
    constructor(context: Context, attr:AttributeSet):super(context,attr)
    var stopScroll = false

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (stopScroll)
            return false
        return super.onTouchEvent(e)
    }
}