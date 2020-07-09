package com.lilas.telcelltestappkt.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.lilas.telcelltestappkt.calback.GetMoviesCallBack
import com.lilas.telcelltestappkt.model.SingleMovieObject
import com.lilas.telcelltestappkt.service.RetrofitClientInstance.client
import com.lilas.telcelltestappkt.service.RetrofiteAPIInterface
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel : ViewModel() {

    private var mSingleMovieObject: SingleMovieObject? = null

    fun getSingleMovie(
        apiKey: String?,
        id: Int,
        singleMovieCallBack: GetMoviesCallBack<SingleMovieObject?>
    ) {
        Log.i("CURRENT_MOVIE_ID", "MOVIE ID ----->$id")
        val retrofiteAPIInterface = client?.create(RetrofiteAPIInterface::class.java)
        val movieObjectObservable = retrofiteAPIInterface?.getSingleMovie(id, apiKey)
        movieObjectObservable?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<SingleMovieObject?> {
                override fun onSubscribe(d: Disposable) {}

                @SuppressLint("CheckResult")
                override fun onNext(singleMovieObject: SingleMovieObject) {
                    mSingleMovieObject = singleMovieObject
                    singleMovieCallBack.onSuccess(singleMovieObject)
                }

                override fun onError(t: Throwable) {
                    singleMovieCallBack.onFailure(t)
                }

                override fun onComplete() {}
            })
    }

    fun getmSingleMovieObject(): SingleMovieObject? {
        return mSingleMovieObject
    }
}