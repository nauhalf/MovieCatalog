package com.dicoding.naufal.moviecatalogue.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.model.Genre
import com.dicoding.naufal.moviecatalogue.model.ProductionCompany
import kotlinx.android.synthetic.main.item_genre.view.*

class GenresAdapter(private val list: MutableList<Genre>) :
    RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addItems(data: List<Genre>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class GenreViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(genre: Genre){
            view.txt_genre.text = genre.name
        }
    }
}