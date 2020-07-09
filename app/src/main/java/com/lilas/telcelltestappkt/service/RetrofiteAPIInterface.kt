package com.lilas.telcelltestappkt.service


import com.lilas.telcelltestappkt.model.MovieObject
import com.lilas.telcelltestappkt.model.SingleMovieObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofiteAPIInterface {
    @GET("movie/popular?")
    fun getMoviesList(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): Observable<MovieObject?>?

    @GET("movie/{movie_id}?")
    fun getSingleMovie(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String?
    ): Observable<SingleMovieObject?>?
}