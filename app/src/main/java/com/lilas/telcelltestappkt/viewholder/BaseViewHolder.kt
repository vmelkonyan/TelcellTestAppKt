package com.lilas.telcelltestappkt.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

open class BaseViewHolder(itemView: View) : ViewHolder(itemView) {
    open fun onBind(position: Int) {}
}