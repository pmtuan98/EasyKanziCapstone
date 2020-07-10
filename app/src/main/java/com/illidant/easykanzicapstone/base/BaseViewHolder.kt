package com.illidant.easykanzicapstone.base


import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var itemData: T? = null
    private var itemPosition: Int = -1

    init {
        itemView.setOnClickListener {
            itemData?.let { onHandleItemClick(it) }
        }
    }

    open fun onBindData(itemData: T) {
        this.itemData = itemData
    }

    open fun onBindData(itemPosition: Int, itemData: T) {
        this.itemPosition = itemPosition
        this.itemData = itemData
    }

    open fun onHandleItemClick(mainItem: T) {
    }
}
