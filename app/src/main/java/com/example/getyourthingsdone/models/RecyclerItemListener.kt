package com.example.getyourthingsdone.models

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerItemListener(context: Context?, recyclerView: RecyclerView,
                                    private val listener: RecyclerTouchListener) : RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector

    interface RecyclerTouchListener {
        fun onClickItem(v: View?, position: Int)
        fun onLongClickItem(v: View?, position: Int)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        return child != null && gestureDetector.onTouchEvent(e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }


    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    init {
        gestureDetector = GestureDetector(context,
                object : SimpleOnGestureListener() {
                    override fun onLongPress(e: MotionEvent) {
                        val view = recyclerView.findChildViewUnder(e.x, e.y)
                        listener.onLongClickItem(view, recyclerView.getChildAdapterPosition(view!!))
                    }

                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        val view = recyclerView.findChildViewUnder(e.x, e.y)
                        listener.onClickItem(view, recyclerView.getChildAdapterPosition(view!!))
                        return true
                    }
                })
    }
}