package com.dicoding.naufal.moviecatalogue.ui.main.favorite.movie


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseFragment
import com.dicoding.naufal.moviecatalogue.databinding.FragmentFavoriteMovieBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity
import com.dicoding.naufal.moviecatalogue.ui.main.FilmAdapter
import com.dicoding.naufal.moviecatalogue.ui.main.FilmItemDecoration
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.FavoriteFilmAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteMovieFragment : BaseFragment<FragmentFavoriteMovieBinding, FavoriteMovieViewModel>() {

    private lateinit var mFavoriteFilmAdapter: FavoriteFilmAdapter
    private val mFavoriteMovieViewModel: FavoriteMovieViewModel by currentScope.viewModel(this)
    private lateinit var mFragmentMovieBinding: FragmentFavoriteMovieBinding

    override fun getLayoutId(): Int = R.layout.fragment_favorite_movie

    override fun getViewModel(): FavoriteMovieViewModel {
        return mFavoriteMovieViewModel
    }

    override fun getBindingVariable(): Int = BR.favoriteMovieViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentMovieBinding = getViewDataBinding()
        setUp()

    }

    private fun setUp() {
        mFavoriteFilmAdapter = FavoriteFilmAdapter(mutableListOf()) {
            startActivity(DetailMovieActivity.newIntent(requireContext(), it.filmId))
        }

        recycler_movie.apply {
            adapter = mFavoriteFilmAdapter
            layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.discovery_columns))
            addItemDecoration(
                FilmItemDecoration(resources.configuration.orientation, 16)
            )
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mFavoriteMovieViewModel.getMovieLiveData().observe(viewLifecycleOwner, Observer {
            mFavoriteFilmAdapter.addFilms(it)
        })

        mFavoriteMovieViewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(mFragmentMovieBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
