package com.dicoding.naufal.moviecatalogue.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object BindingUtils{

    @BindingAdapter("clipToOutline")
    @JvmStatic
    fun setClipToOutline(imageView: ImageView, state: Boolean){
        imageView.clipToOutline = state
    }
}