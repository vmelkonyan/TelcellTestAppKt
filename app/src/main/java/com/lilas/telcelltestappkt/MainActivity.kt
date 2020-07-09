package com.lilas.telcelltestappkt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lilas.telcelltestappkt.adapter.MoviesAdapter
import com.lilas.telcelltestappkt.calback.GetMoviesCallBack
import com.lilas.telcelltestappkt.constants.AppConstants
import com.lilas.telcelltestappkt.databinding.ActivityMainBinding
import com.lilas.telcelltestappkt.listener.PaginationListener
import com.lilas.telcelltestappkt.model.MovieObject
import com.lilas.telcelltestappkt.model.ResultMovie
import com.lilas.telcelltestappkt.viewmodel.ListViewModel
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(), MoviesAdapter.ItemClickListener {
    private var mMoviesAdapter: MoviesAdapter? = null
    private var mCurrentPage = 1
    var mIsLastPage = false
        private set
    private var mTotalPage = 0
    var mIsLoading = false
        private set
    private var mListViewModel: ListViewModel? = null
    private val mGetMoviesCallBack = object : GetMoviesCallBack<MovieObject> {
        override fun onSuccess(movie: MovieObject) {
            mTotalPage = movie.totalPages ?: 0
            Observable.fromIterable(movie.resultMovies)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .filter { movieModel: ResultMovie -> movieModel.adult == false }
                .toList()
                .subscribe(object : SingleObserver<List<ResultMovie>> {
                    override fun onSubscribe(d: Disposable) {
                        /* to do */
                    }

                    override fun onSuccess(resultMovies: List<ResultMovie>) {
                        handleResults(resultMovies)
                    }

                    override fun onError(e: Throwable) {
                        /* to do */
                    }
                })
        }

        override fun onFailure(t: Throwable?) {
            handleError(t)
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        mListViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        val recyclerView = activityMainBinding.viewMovies
        val verticalLayoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = verticalLayoutManager
        recyclerView.setHasFixedSize(true)
        mMoviesAdapter = MoviesAdapter()
        mMoviesAdapter?.setClickListener(this)
        recyclerView.adapter = mMoviesAdapter

        mListViewModel?.let {
            if (it.getResultMovieList().isNotEmpty()) {
                mCurrentPage = it.currentPage
                mMoviesAdapter?.setmMoviesList(it.getResultMovieList())
            } else {
                it.getPopularMovies(getString(R.string.API_KEY), mCurrentPage, mGetMoviesCallBack)
            }
        }

        recyclerView.addOnScrollListener(object : PaginationListener(verticalLayoutManager) {
            override fun loadMoreItems() {
                mIsLoading = true
                mCurrentPage++
                mListViewModel?.getPopularMovies(
                    getString(R.string.API_KEY),
                    mCurrentPage,
                    mGetMoviesCallBack
                )
            }

            override fun isLastPage(): Boolean {
                return mIsLastPage
            }

            override fun isLoading(): Boolean {
                return mIsLoading
            }

        })
    }

    private fun handleResults(resultMovieList: List<ResultMovie>?) {
        if (resultMovieList != null && resultMovieList.isNotEmpty()) {
            val mMoviesAdapter = this.mMoviesAdapter ?: return
            if (mCurrentPage != 1) {
                mMoviesAdapter.removeLoading()
            }
            mMoviesAdapter.setmMoviesList(resultMovieList)
            if (mCurrentPage < mTotalPage) {
                mMoviesAdapter.addLoading()
            } else {
                mIsLastPage = true
            }
            mIsLoading = false
        } else {
            Toast.makeText(
                this, "NO RESULTS FOUND",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleError(t: Throwable?) {
        Toast.makeText(
            this, "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onItemClick(view: View?, id: Int) {
        val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
        intent.putExtra(AppConstants.SELECTED_MOVIE_ID, id)
        startActivity(intent)
    }
}