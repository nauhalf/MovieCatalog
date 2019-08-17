package com.dicoding.naufal.moviecatalogue.model

import com.google.gson.annotations.SerializedName

data class Creator(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String? = null
)