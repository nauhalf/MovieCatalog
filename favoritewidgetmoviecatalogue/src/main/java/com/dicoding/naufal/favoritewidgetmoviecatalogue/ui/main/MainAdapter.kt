package com.dicoding.naufal.favoritewidgetmoviecatalogue.ui.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.favoritewidgetmoviecatalogue.R
import com.dicoding.naufal.favoritewidgetmoviecatalogue.data.model.FavoriteFilm
import com.dicoding.naufal.favoritewidgetmoviecatalogue.utils.MovieUtils
import kotlinx.android.synthetic.main.item_film.view.*

class MainAdapter(private val list: MutableList<FavoriteFilm> = mutableListOf(), private val onClickListener: (FavoriteFilm) -> Unit) :
    RecyclerView.Adapter<MainAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
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

            view.txt_type.text =
                if (film.filmType == 1) view.context.getString(R.string.movie) else view.context.getString(R.string.tv)
            view.setOnClickListener {
                onClickListener(film)
            }
        }
    }

}