package com.code4sk.justlearn

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView



class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, listener: OnRecyclerTouchListener): RecyclerView.SimpleOnItemTouchListener() {

    interface OnRecyclerTouchListener{
        fun onSingleTap(view: View, position: Int)
        fun onLongTap(view: View, position: Int)

    }

    private val gestureDetectorObject = object: GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
//            Log.d(tag, "single tap: $e")
            val childView = recyclerView.findChildViewUnder(e.x, e.y)!!
            listener.onSingleTap(childView, recyclerView.getChildAdapterPosition(childView))
            return true
        }

        override fun onLongPress(e: MotionEvent) {
//            Log.d(tag, "long tap: $e")
            val childView = recyclerView.findChildViewUnder(e.x, e.y)!!
            listener.onLongTap(childView, recyclerView.getChildAdapterPosition(childView))
            super.onLongPress(e)
        }
    }

    private val gestureDetector = GestureDetectorCompat(context, gestureDetectorObject)
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val result = gestureDetector.onTouchEvent(e)
//        Log.d(tag, "onInterceptTouchEvent $result")
        return result
//        return super.onInterceptTouchEvent(rv, e)
    }


}