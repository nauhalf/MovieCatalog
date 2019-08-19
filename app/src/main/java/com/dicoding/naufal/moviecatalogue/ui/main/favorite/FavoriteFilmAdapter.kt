package com.dicoding.naufal.moviecatalogue.ui.main.favorite


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.data.local.db.favorite.FavoriteFilm
import com.dicoding.naufal.moviecatalogue.model.Film
import com.dicoding.naufal.moviecatalogue.utils.MovieUtils
import kotlinx.android.synthetic.main.item_favorite_film.view.*

class FavoriteFilmAdapter(private val list: MutableList<FavoriteFilm>, private val onClickListener: (FavoriteFilm) -> Unit) :
    RecyclerView.Adapter<FavoriteFilmAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_film, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addFilms(data: List<FavoriteFilm>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class FilmViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(film: FavoriteFilm) {
            view.txt_film_name.text = film.filmTitle

            Glide.with(view.context)
                .load(MovieUtils.getImagePoster(film.filmPosterUrl))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .into(view.img_film_cover)

            view.setOnClickListener {
                onClickListener(film)
            }
        }
    }

}