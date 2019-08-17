package com.dicoding.naufal.moviecatalogue.model

import com.google.gson.annotations.SerializedName

data class Films(
    @SerializedName("page")
    var page: Int = 0,

    @SerializedName("total_results")
    var totalResults: Int =0,

    @SerializedName("total_pages")
    var totalPages: Int = 0,

    @SerializedName("results")
    var film: List<Film>? = null
)