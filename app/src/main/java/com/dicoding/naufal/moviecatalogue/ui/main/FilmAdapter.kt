package com.dicoding.naufal.moviecatalogue.ui.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.utils.MovieUtils
import kotlinx.android.synthetic.main.item_film.view.*

class FilmAdapter(private val list: MutableList<Film>, private val onClickListener: (Film) -> Unit) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addFilms(data: List<Film>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class FilmViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(film: Film) {
            view.txt_film_name.text = film.title

            view.txt_rate.text = film.voteAverage.toString()

            Glide.with(view.context)
                .load(MovieUtils.getImagePoster(film.posterPath))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(view.img_film_cover)

            view.setOnClickListener {
                onClickListener(film)
            }
        }
    }

}