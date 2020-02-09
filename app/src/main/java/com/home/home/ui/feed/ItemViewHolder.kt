package com.home.home.ui.feed

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.home.home.R
import com.home.home.domain.models.Feed
import kotlinx.android.synthetic.main.item_feed.view.*

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Feed) {
        itemView.tv_name.text = item.name
        itemView.tv_weight.text = item.weight
        itemView.circle_color.background.setColorFilter(
            Color.parseColor(item.bagColor),
            PorterDuff.Mode.SRC_IN
        )
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed, parent, false)
            return ItemViewHolder(view)
        }
    }

}