package com.dicoding.naufal.moviecatalogue.ui.main.home.movie


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseFragment
import com.dicoding.naufal.moviecatalogue.databinding.FragmentMovieBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity
import com.dicoding.naufal.moviecatalogue.ui.main.FilmAdapter
import com.dicoding.naufal.moviecatalogue.ui.main.FilmItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieFragment : BaseFragment<FragmentMovieBinding, MovieViewModel>() {

    private lateinit var mFilmAdapter: FilmAdapter
    private val mMovieViewModel: MovieViewModel by currentScope.viewModel(this)
    private lateinit var mFragmentMovieBinding: FragmentMovieBinding

    override fun getLayoutId(): Int = R.layout.fragment_movie

    override fun getViewModel(): MovieViewModel {
        return mMovieViewModel
    }

    override fun getBindingVariable(): Int = BR.movieViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentMovieBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp() {
        mFilmAdapter = FilmAdapter(mutableListOf()) {
            startActivity(DetailMovieActivity.newIntent(requireContext(), it.id))
        }

        recycler_movie.apply {
            adapter = mFilmAdapter
            layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.discovery_columns))
            addItemDecoration(
                FilmItemDecoration(resources.configuration.orientation, 16)
            )
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mMovieViewModel.getMovieLiveData().observe(viewLifecycleOwner, Observer {
            mFilmAdapter.addFilms(it)
        })

        mMovieViewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(mFragmentMovieBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

}
