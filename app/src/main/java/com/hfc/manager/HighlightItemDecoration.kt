package com.hfc.manager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HighlightItemDecoration(
    highlightColor: Int,
    private val itemWidth: Int,
    private val itemHeight: Int,
    private val itemPadding: Int,
) : RecyclerView.ItemDecoration() {
    private var paintSelect = Paint()
    private var paintUnSelect = Paint()
    private var highlightPosition = -1
    private var lastPosition = -1

    init {
        paintSelect.color = highlightColor
        paintSelect.style = Paint.Style.FILL
        paintUnSelect.color = Color.TRANSPARENT
        paintUnSelect.style = Paint.Style.FILL
    }

    fun setHighlightPosition(position: Int) {
        Log.d("liyu", "setHighlightPosition() called with: position = $position")
        if (highlightPosition == position) return
        if (position == -1) {
            lastPosition = highlightPosition
        }
        highlightPosition = position
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (highlightPosition != -1) {
            val viewHolder = parent.findViewHolderForAdapterPosition(highlightPosition)
            if (viewHolder?.itemView != null) {
                val child = viewHolder.itemView
                val left: Int = child.left + itemPadding
                val top: Int = child.top + itemPadding
                val right = left + itemWidth
                val bottom = top + itemHeight
                c.drawRoundRect(
                    left.toFloat(), top.toFloat(),
                    right.toFloat(), bottom.toFloat(), 30f, 30f,
                    paintSelect
                )
            }
        } else {
            val viewHolder = parent.findViewHolderForAdapterPosition(lastPosition)
            Log.d("yuli", "onDraw() called with: c = $lastPosition ${viewHolder?.itemView}")

            if (viewHolder?.itemView != null) {
                val child = viewHolder.itemView
                val left: Int = child.left + itemPadding
                val top: Int = child.top + itemPadding
                val right = left + itemWidth
                val bottom = top + itemHeight
                c.drawRect(
                    left.toFloat(), top.toFloat(),
                    right.toFloat(), bottom.toFloat(),
                    paintUnSelect
                )
            }
            lastPosition = -1
        }
    }
}