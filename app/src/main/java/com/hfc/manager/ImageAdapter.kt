package com.hfc.manager

import android.content.Context
import android.content.pm.ResolveInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.hfc.manager.databinding.ItemImgBinding

class ImageAdapter : BaseQuickAdapter<ResolveInfo, ImageAdapter.VH>() {
    private lateinit var scaleUpAnimation: Animation
    private lateinit var scaleDownAnimation: Animation

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!::scaleUpAnimation.isInitialized) {
            scaleUpAnimation = AnimationUtils.loadAnimation(
                context,
                R.anim.item_anim_up
            )
        }
        if (!::scaleDownAnimation.isInitialized) {
            scaleDownAnimation = AnimationUtils.loadAnimation(
                context,
                R.anim.item_anim_down
            )
        }
    }

    var curPosition: Int = -1

    // 自定义ViewHolder类
    class VH(
        parent: ViewGroup,
        val binding: ItemImgBinding = ItemImgBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        // 返回一个 ViewHolder
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: ResolveInfo?) {
        // 设置item数据
        item?.let { info ->
            holder.binding.itemImg.setImageDrawable(info.loadIcon(context.packageManager))
            holder.binding.itemLabel.text = info.loadLabel(context.packageManager)
            /*holder.binding.root.setOnClickListener {
                Log.e("yyyy", "onBindViewHolder: " +
                        "${info.activityInfo.packageName}--${info.activityInfo.name}")
            }*/
            Log.d(
                "yuli",
                "onBindViewHolder() called with: info = $curPosition ${holder.absoluteAdapterPosition}"
            )
            if (curPosition == holder.absoluteAdapterPosition) {
                holder.binding.itemLabel.isSelected = true
//                holder.binding.itemContent.setBackgroundResource(R.drawable.item_bg)
                holder.binding.itemImg.startAnimation(scaleUpAnimation)
            } else {
                holder.binding.itemLabel.isSelected = false
                holder.binding.itemImg.startAnimation(scaleDownAnimation)
//                holder.binding.itemContent.setBackgroundColor(context.getColor(android.R.color.transparent))
            }
        }
    }

    fun dragPosition(position: Int) {
        Log.d("yuli", "dragPosition() called with: position = $position $curPosition")
        if (curPosition == position) return
        val lastPosition: Int = curPosition
        curPosition = position
        if (lastPosition != -1)
            notifyItemChanged(lastPosition)
        notifyItemChanged(position)
    }

    fun dragExit() {
        Log.d("yuli", "dragExit() called with: position =  $curPosition")
        val lastPosition: Int = curPosition
        curPosition = -1
        notifyItemChanged(lastPosition)
    }
}