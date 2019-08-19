package com.dicoding.naufal.moviecatalogue.ui.main.favorite.tv

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseFragment
import com.dicoding.naufal.moviecatalogue.databinding.FragmentFavoriteTvShowBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity
import com.dicoding.naufal.moviecatalogue.ui.main.FilmItemDecoration
import com.dicoding.naufal.moviecatalogue.ui.main.favorite.FavoriteFilmAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_tv_show.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTvShowFragment : BaseFragment<FragmentFavoriteTvShowBinding, FavoriteTvShowViewModel>() {

    private lateinit var mFavoriteFilmAdapter: FavoriteFilmAdapter
    private val mTvShowViewModel: FavoriteTvShowViewModel by currentScope.viewModel(this)
    private lateinit var mTvShowBinding: FragmentFavoriteTvShowBinding

    override fun getLayoutId(): Int = R.layout.fragment_favorite_tv_show

    override fun getViewModel(): FavoriteTvShowViewModel {
        return mTvShowViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.favoriteTvShowViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTvShowBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp() {
        mFavoriteFilmAdapter = FavoriteFilmAdapter(mutableListOf()) {
            startActivity(DetailTvShowActivity.newIntent(requireContext(), it.filmId))
        }

        recycler_tv.apply {
            adapter = mFavoriteFilmAdapter
            layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.discovery_columns))
            addItemDecoration(
                FilmItemDecoration(resources.configuration.orientation, 16)
            )
        }

        subscribeToLiveData()
    }


    private fun subscribeToLiveData() {

        mTvShowViewModel.getTvLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mFavoriteFilmAdapter.addFilms(it)
            }
        })

        mTvShowViewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(mTvShowBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        })
    }
}
