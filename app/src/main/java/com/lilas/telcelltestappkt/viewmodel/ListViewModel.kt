package com.lilas.telcelltestappkt.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.lilas.telcelltestappkt.calback.GetMoviesCallBack
import com.lilas.telcelltestappkt.model.MovieObject
import com.lilas.telcelltestappkt.model.ResultMovie
import com.lilas.telcelltestappkt.service.RetrofitClientInstance.client
import com.lilas.telcelltestappkt.service.RetrofiteAPIInterface
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ListViewModel : ViewModel() {
    private val resultMovieList: MutableList<ResultMovie> = ArrayList()
    var currentPage = 0
        private set

    fun getPopularMovies(
        apiKey: String?,
        page: Int,
        getMoviesCallBack: GetMoviesCallBack<MovieObject>
    ) {
        currentPage = page
        Log.i("CURRENT_PAGE_INDEX", "CURRENT PAGE ----->$page")
        val retrofiteAPIInterface = client?.create(RetrofiteAPIInterface::class.java)
        val movieObjectObservable = retrofiteAPIInterface?.getMoviesList(apiKey, page)
        movieObjectObservable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object : Observer<MovieObject?> {
            override fun onSubscribe(d: Disposable) {}

            @SuppressLint("CheckResult")
            override fun onNext(movieObject: MovieObject) {
                movieObject.resultMovies?.let {
                    resultMovieList.addAll(it)
                }
                getMoviesCallBack.onSuccess(movieObject)
            }

            override fun onError(t: Throwable) {
                getMoviesCallBack.onFailure(t)
            }

            override fun onComplete() {}
        })
    }

    fun getResultMovieList(): List<ResultMovie> {
        return resultMovieList
    }

}