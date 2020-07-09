package com.lilas.telcelltestappkt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lilas.telcelltestappkt.R
import com.lilas.telcelltestappkt.databinding.MoviesItemBinding
import com.lilas.telcelltestappkt.databinding.ProgressBarItemBinding
import com.lilas.telcelltestappkt.model.ResultMovie
import com.lilas.telcelltestappkt.viewholder.BaseViewHolder

import java.util.*

class MoviesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var mIsLoaderVisible = false
    private var mClickListener: ItemClickListener? = null
    private val mMoviesList: MutableList<ResultMovie> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val moviesItemBinding: MoviesItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.movies_item, parent, false
                )
                MoviesViewHolder(moviesItemBinding)
            }
            else -> {
                val progressBarItemBinding: ProgressBarItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.progress_bar_item, parent, false
                )
                ProgressViewHolder(progressBarItemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mIsLoaderVisible) {
            if (position == mMoviesList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun addLoading() {
        mIsLoaderVisible = true
        mMoviesList.add(ResultMovie())
        notifyItemInserted(mMoviesList.size - 1)
    }

    fun removeLoading() {
        mIsLoaderVisible = false
        val position = mMoviesList.size - 1
        mMoviesList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return mMoviesList.size
    }

    inner class MoviesViewHolder(private val moviesItemBinding: MoviesItemBinding) :
        BaseViewHolder(moviesItemBinding.root) {
        override fun onBind(position: Int) {
            super.onBind(position)
            val resultMovie = mMoviesList[position]
            moviesItemBinding.moviItem = resultMovie
            moviesItemBinding.root.setOnClickListener { view: View? ->
                mMoviesList[adapterPosition].id?.let {
                    mClickListener?.onItemClick(view, it)
                }
            }
        }

    }

    class ProgressViewHolder(progressBarItemBinding: ProgressBarItemBinding) :
        BaseViewHolder(progressBarItemBinding.root)

    fun setmMoviesList(mMoviesList: List<ResultMovie>) {
        this.mMoviesList.addAll(mMoviesList)
        notifyDataSetChanged()
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, id: Int)
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}