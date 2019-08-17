package com.dicoding.naufal.moviecatalogue.ui.detail.tv

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CreatorItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) > 0) {
            outRect.left = padding
        }
    }
}