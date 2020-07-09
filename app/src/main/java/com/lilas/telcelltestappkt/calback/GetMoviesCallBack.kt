package com.lilas.telcelltestappkt.calback


interface GetMoviesCallBack<T> {
    fun onSuccess(movie: T)
    fun onFailure(t: Throwable?)
}