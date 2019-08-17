package com.dicoding.naufal.moviecatalogue.model

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("logo_path")
    var logoPath: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("origin_country")
    var originCountry: String? = null
)