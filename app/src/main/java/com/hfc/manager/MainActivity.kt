package com.hfc.manager

import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfc.manager.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val imgAdapter by lazy { ImageAdapter() }
    private val imgs = arrayListOf(
        R.drawable.test1, R.drawable.test2, R.drawable.test3,
        R.drawable.test4, R.drawable.test5, R.drawable.test6, R.drawable.test7
    )
    private val blankFragment by lazy { BlankFragment.newInstance() }
    private val viewModel by lazy { ViewModelProvider(this)[BlankViewModel::class.java] }
    private var rvIsScrolling: Boolean = false
    private lateinit var itemDecoration: HighlightItemDecoration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imgRv.adapter = imgAdapter
        binding.imgRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imgAdapter.submitList(collectShareAction())

        binding.imgRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d(
                    "yuli",
                    "onScrollStateChanged() called with: recyclerView = $recyclerView, newState = $newState"
                )
                if (newState == RecyclerView.SCROLL_STATE_IDLE && rvIsScrolling) {
                    // 滑动结束
                    rvIsScrolling = false
                }
            }
        })
        binding.imgRv.itemAnimator = null
        val itemWidth = resources.getDimensionPixelSize(R.dimen.dp60)
        val itemHeight = resources.getDimensionPixelSize(R.dimen.dp110)
        val itemPadding = resources.getDimensionPixelSize(R.dimen.dp10)
        itemDecoration = HighlightItemDecoration(Color.parseColor("#9CAFACAC"), itemWidth, itemHeight, itemPadding)
        binding.imgRv.addItemDecoration(itemDecoration)
        binding.imgRv.setOnDragListener { v, event ->
            if (event.action == DragEvent.ACTION_DROP) {
                Log.d("yyyy", "ACTION_DROP")
                /*val count = event.clipData.itemCount
                if (count > 0) {
                    val data = event.clipData.getItemAt(0)
                    val type = event.clipDescription.getMimeType(0)
                    if (type.startsWith("text/")) {
                        Log.e("yyyy", "text: ${data.text}")
                    }
                }*/
                // 拖拽释放
                clearHighlight()
            } else if (event.action == DragEvent.ACTION_DRAG_ENTERED) {
                Log.d("yyyy", "ACTION_DRAG_ENTERED")
            } else if (event.action == DragEvent.ACTION_DRAG_EXITED) {
                Log.d("yyyy", "ACTION_DRAG_EXITED")
                clearHighlight()
            } else if (event.action == DragEvent.ACTION_DRAG_STARTED) {
                Log.d("yyyy", "ACTION_DRAG_STARTED")
            } else if (event.action == DragEvent.ACTION_DRAG_LOCATION) {
                Log.d("yyyy", "ACTION_DRAG_LOCATION ${event.x},${event.y}")
                val position: Int = getPositionUnderDrag(event)
                if (position != -1) {
                    highlightItem(position)
                    itemDecoration.setHighlightPosition(position)
                }
                checkAutoScroll(event)
            }
            true
        }

        binding.tvDrag.setOnLongClickListener {
            it.startDragAndDrop(
                ClipData.newPlainText("text/plain", binding.tvDrag.text),
                View.DragShadowBuilder(it), null, View.DRAG_FLAG_GLOBAL
            )
            true
        }
        /*val bt = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag("blankFragment")
        if (fragment == null) {
            bt.add(R.id.fragmentContainer, blankFragment, "blankFragment")
                .show(blankFragment)
                .commitNowAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction()
                .show(blankFragment)
                .commitNowAllowingStateLoss()
        }
        if (viewModel.getData() == null) {
            viewModel.setData("7895")
        }*/
    }

    private fun getPositionUnderDrag(event: DragEvent): Int {
        val x: Float = event.x + binding.imgRv.computeHorizontalScrollOffset()
        val totalWidth: Int = binding.imgRv.computeHorizontalScrollRange()
        val itemWidth: Int = totalWidth / imgAdapter.itemCount
        val position = (x / itemWidth).toInt()
        return if (position >= 0 && position < imgAdapter.itemCount) {
            position
        } else -1
    }

    /*private fun getPositionUnderDrag(event: DragEvent): Int {
        val rect = Rect()
        for (i in 0 until binding.imgRv.childCount) {
            val child: View = binding.imgRv.getChildAt(i)
            child.getHitRect(rect)
            if (rect.contains(event.x.toInt(), event.y.toInt())) {
                return binding.imgRv.getChildAdapterPosition(child)
            }
        }
        return -1
    }*/

    private fun checkAutoScroll(event: DragEvent) {
        if (rvIsScrolling) {
            Log.d("yuli", "防止滑动太快")
            return
        }
        val x = event.x.toInt()
        val width: Int = binding.imgRv.width
        if (x < 50 && binding.imgRv.computeHorizontalScrollOffset() > 0) {
            // 拖拽到顶部边缘，向上滚动
            rvIsScrolling = true
            binding.imgRv.smoothScrollBy(-binding.imgRv.width, 0)
        } else if (x > width - 50 &&
            binding.imgRv.computeHorizontalScrollOffset() < binding.imgRv.computeHorizontalScrollRange() - width
        ) {
            // 拖拽到底部边缘，向下滚动
            rvIsScrolling = true
            binding.imgRv.smoothScrollBy(binding.imgRv.width, 0)
        }
    }

    private fun highlightItem(position: Int) {
        imgAdapter.dragPosition(position)
    }

    private fun clearHighlight() {
        imgAdapter.dragExit()
        itemDecoration.setHighlightPosition(-1)
    }

    private fun collectShareAction(): MutableList<ResolveInfo> {
        val intent = Intent(Intent.ACTION_SEND, null)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.type = "*/*"
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
            )
        }
    }
}