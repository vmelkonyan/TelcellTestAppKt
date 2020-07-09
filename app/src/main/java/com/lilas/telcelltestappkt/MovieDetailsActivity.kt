package com.lilas.telcelltestappkt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.lilas.telcelltestappkt.calback.GetMoviesCallBack
import com.lilas.telcelltestappkt.constants.AppConstants
import com.lilas.telcelltestappkt.databinding.DetailsActivityBinding
import com.lilas.telcelltestappkt.model.SingleMovieObject
import com.lilas.telcelltestappkt.viewmodel.DetailsViewModel


class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)
        val mDetailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        val detailsActivityBinding =
            DataBindingUtil.setContentView<DetailsActivityBinding>(this, R.layout.details_activity)
                .apply {
                    singleMovie = mDetailsViewModel.getmSingleMovieObject()
                }

        val id = intent.getIntExtra(AppConstants.SELECTED_MOVIE_ID, 0)

        if (mDetailsViewModel.getmSingleMovieObject() == null) {
            mDetailsViewModel.getSingleMovie(getString(R.string.API_KEY), id, object :
                GetMoviesCallBack<SingleMovieObject?> {
                override fun onSuccess(movie: SingleMovieObject?) {
                    detailsActivityBinding.singleMovie = movie
                }

                override fun onFailure(t: Throwable?) {
                    handleError(t)
                }
            })
        }
    }

    private fun handleError(t: Throwable?) {
        Toast.makeText(
            this, "ERROR IN FETCHING API RESPONSE. Try again",
            Toast.LENGTH_LONG
        ).show()
    }
}