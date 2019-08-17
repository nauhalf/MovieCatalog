package com.dicoding.naufal.moviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("title", alternate = ["name"])
    var title: String? = null,

    @SerializedName("release_date", alternate = ["first_air_date"])
    var releaseDate: String? = null,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("original_title", alternate = ["original_name"])
    var originalName:String? = null,

    @SerializedName("original_language")
    var originalLanguage: String? = null,

    @SerializedName("poster_path")
    var posterPath: String? = null,

    @SerializedName("vote_average")
    var voteAverage: Double = 0.0
) : Parcelable