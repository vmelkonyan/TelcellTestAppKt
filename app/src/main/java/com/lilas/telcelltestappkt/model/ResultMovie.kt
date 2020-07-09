package com.lilas.telcelltestappkt.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultMovie : Parcelable {
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("video")
    @Expose
    var video: Boolean? = null

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null

    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null

    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null

    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null

    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    constructor() {}
    constructor(`in`: Parcel) {
        popularity = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readDouble()
        }
        id = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readInt()
        }
        val tmpVideo = `in`.readByte()
        video = if (tmpVideo.toInt() == 0) null else tmpVideo.toInt() == 1
        voteCount = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readInt()
        }
        voteAverage = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readDouble()
        }
        title = `in`.readString()
        releaseDate = `in`.readString()
        originalLanguage = `in`.readString()
        originalTitle = `in`.readString()
        backdropPath = `in`.readString()
        val tmpAdult = `in`.readByte()
        adult = if (tmpAdult.toInt() == 0) null else tmpAdult.toInt() == 1
        overview = `in`.readString()
        posterPath = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        popularity?.let {
            dest.writeByte(1.toByte())
            dest.writeDouble(it)
        } ?: dest.writeByte(0.toByte())

        id?.let {
            dest.writeByte(1.toByte())
            dest.writeInt(it)
        } ?: dest.writeByte(0.toByte())

        dest.writeByte((if (video == null) 0 else if (video as Boolean) 1 else 2).toByte())

        voteCount?.let {
            dest.writeByte(1.toByte())
            dest.writeInt(it)
        } ?: dest.writeByte(0.toByte())

        voteAverage?.let {
            dest.writeByte(1.toByte())
            dest.writeDouble(it)
        } ?: dest.writeByte(0.toByte())

        dest.writeString(title)
        dest.writeString(releaseDate)
        dest.writeString(originalLanguage)
        dest.writeString(originalTitle)
        dest.writeString(backdropPath)
        dest.writeByte((if (adult == null) 0 else if (adult as Boolean) 1 else 2).toByte())
        dest.writeString(overview)
        dest.writeString(posterPath)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ResultMovie?> = object : Parcelable.Creator<ResultMovie?> {
            override fun createFromParcel(`in`: Parcel): ResultMovie? {
                return ResultMovie(`in`)
            }

            override fun newArray(size: Int): Array<ResultMovie?> {
                return arrayOfNulls(size)
            }
        }
    }
}