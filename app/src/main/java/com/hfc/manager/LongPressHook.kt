package com.hfc.manager

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.IXposedHookZygoteInit.StartupParam
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


class LongPressHook : IXposedHookZygoteInit {
    private var downTime: Long = 0
    private var pressedView: View? = null

    @Throws(Throwable::class)
    override fun initZygote(startupParam: StartupParam) {
       /* XposedBridge.hookAllMethods(View::class.java, "performLongClick", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {

                // 在长按事件触发前执行
                val view = param.thisObject as View
                handleLongPress(view)
            }
        })*/
        XposedBridge.hookAllMethods(
            ViewGroup::class.java,
            "dispatchTouchEvent",
            object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    handleTouchEvent(param.thisObject as View, param.args[0] as MotionEvent)
                }
            })
        XposedBridge.hookAllMethods(View::class.java, "dispatchTouchEvent", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                handleTouchEvent(param.thisObject as View, param.args[0] as MotionEvent)
            }

        })
    }

    private fun handleTouchEvent(view: View, event: MotionEvent) {
        val action = event.actionMasked
        XposedBridge.log("Long press downTime:${event.downTime} ${event.eventTime}" )
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                downTime = event.downTime
                pressedView = view
            }
            MotionEvent.ACTION_UP -> {
                if (event.eventTime - downTime >= LONG_PRESS_TIMEOUT_MS && view === pressedView) {
                    XposedBridge.log("Long press detected on view: " + view.javaClass.name)
                }
                pressedView = null
            }
        }
    }

    private fun handleLongPress(view: View) {

        // 获取长按View的文本内容
        val text = getViewText(view)
        if (text != null) {
            XposedBridge.log("Long press detected on view with text: $text")

            // 在这里处理文本内容，如复制到剪贴板、记录日志等
        } else {
            XposedBridge.log("Long press detected on a non-textual view.")
        }
    }


    private fun getViewText(view: View): String? {
        if (view is TextView) {
            return view.text.toString()
        } else if (view is ViewGroup) {

            // Recursively search for text in child views
            val group = view
            for (i in 0 until group.childCount) {
                val childText = getViewText(group.getChildAt(i))
                if (childText != null) {
                    return childText
                }
            }
        }
        return null
    }

    companion object {
        private const val LONG_PRESS_TIMEOUT_MS = 500 // 长按判定时间阈值，可根据需求调整
    }
}