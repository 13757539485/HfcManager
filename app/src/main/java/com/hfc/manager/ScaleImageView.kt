package com.hfc.manager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.ContentFrameLayout

class ScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var currentIndex = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            this.elevation = 1f
            // 获取下标
            currentIndex = (this.parent as ViewGroup).indexOfChild(this)
            zIndex(this, false)
            animScale(1.0f, 1.1f) {

            }
        } else if (event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_CANCEL) {
            animScale(1.1f, 1.0f) {
                treeView[treeView.size-1].removeView(this)
                treeView[0].removeViewAt(currentIndex)
                treeView[0].addView(this, currentIndex)
                this.elevation = 0f
                treeView.clear()
            }
        }
        return true
    }
    private val treeView = mutableListOf<ViewGroup>()

    private val emptyView = View(context)

    private fun zIndex(view: View, isRecovery: Boolean) {
        val vp = view.parent as ViewGroup
        if (vp is ContentFrameLayout &&
            resources.getResourceName(vp.id) == "android:id/content") {
            treeView.add(vp)
            treeView[0].removeView(this)
            emptyView.layoutParams = this.layoutParams
            treeView[0].addView(emptyView, currentIndex)
            treeView[treeView.size-1].addView(this)
            return
        }
        treeView.add(vp)
        zIndex(vp, isRecovery)
    }

    private inline fun animScale(from: Float, to: Float, crossinline block: () -> Unit) {
        val scale = ScaleAnimation(from, to, from, to, 0.5f, 0.5f).also {
            it.duration = 500
            it.fillAfter = true
        }
        scale.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                block()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        startAnimation(scale)
    }
}