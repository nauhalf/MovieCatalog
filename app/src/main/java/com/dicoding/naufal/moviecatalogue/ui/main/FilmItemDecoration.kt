package com.dicoding.naufal.moviecatalogue.ui.main

import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FilmItemDecoration(private val orientation: Int, private val padding: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when(orientation){
            Configuration.ORIENTATION_PORTRAIT -> {
                if(parent.getChildAdapterPosition(view) > 1){
                    outRect.top = padding
                }

                if((parent.getChildAdapterPosition(view)+1) % 2 == 0){
                    outRect.left = padding
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                if(parent.getChildAdapterPosition(view) > 2){
                    outRect.top = padding
                }


                if((parent.getChildAdapterPosition(view)+1) % 2 == 0){
                    outRect.left = padding
                    outRect.right = padding
                }
            }
        }

    }
}