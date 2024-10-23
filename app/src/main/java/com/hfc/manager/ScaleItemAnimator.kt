package com.hfc.manager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator


class ScaleItemAnimator: SimpleItemAnimator() {
    private val pendingChanges: MutableList<ChangeInfo> = ArrayList()

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        if (oldHolder != null && newHolder != null) {
            pendingChanges.add(ChangeInfo(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop))
            return true
        }
        return false
    }

    override fun runPendingAnimations() {
        for (change in pendingChanges) {
            animateChangeImpl(change)
        }
        pendingChanges.clear()
    }

    private fun animateChangeImpl(change: ChangeInfo) {
        val oldView = ((change.oldHolder.itemView) as ViewGroup).getChildAt(0)
        val newView = ((change.newHolder.itemView) as ViewGroup).getChildAt(0)
        oldView.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    dispatchChangeFinished(change.oldHolder, true)
                }
            })
            .start()
//        newView.scaleX = 0f
//        newView.scaleY = 0f
        newView.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    dispatchChangeFinished(change.newHolder, false)
                }
            })
            .start()
    }

    override fun endAnimations() {}

    override fun isRunning(): Boolean {
        return !pendingChanges.isEmpty()
    }

    private class ChangeInfo internal constructor(
        var oldHolder: RecyclerView.ViewHolder,
        var newHolder: RecyclerView.ViewHolder,
        var fromLeft: Int,
        var fromTop: Int,
        var toLeft: Int,
        var toTop: Int
    )

    override fun endAnimation(item: RecyclerView.ViewHolder) {
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        return false
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        return false
    }

}