package com.lilas.telcelltestappkt.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(private val mLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = mLayoutManager.childCount
        val totalItemCount = mLayoutManager.itemCount
        val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

}