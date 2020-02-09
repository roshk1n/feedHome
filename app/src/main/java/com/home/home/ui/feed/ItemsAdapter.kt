package com.home.home.ui.feed

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.home.domain.models.Feed

class ItemsAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private var data: ArrayList<Feed> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addItem(item: Feed) {
        this.data.add(0, item)
        notifyItemInserted(0)
    }
}