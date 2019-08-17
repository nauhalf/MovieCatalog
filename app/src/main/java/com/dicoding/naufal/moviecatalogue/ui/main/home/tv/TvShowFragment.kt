package com.dicoding.naufal.moviecatalogue.ui.main.home.tv

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseFragment
import com.dicoding.naufal.moviecatalogue.databinding.FragmentTvShowBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity
import com.dicoding.naufal.moviecatalogue.ui.main.FilmAdapter
import com.dicoding.naufal.moviecatalogue.ui.main.FilmItemDecoration
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_tv_show.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvShowFragment : BaseFragment<FragmentTvShowBinding, TvShowViewModel>() {

    private lateinit var mFilmAdapter: FilmAdapter
    private val mTvShowViewModel: TvShowViewModel by currentScope.viewModel(this)
    private lateinit var mTvShowBinding: FragmentTvShowBinding

    override fun getLayoutId(): Int = R.layout.fragment_tv_show

    override fun getViewModel(): TvShowViewModel {
        return mTvShowViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.tvShowViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTvShowBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp() {
        mFilmAdapter = FilmAdapter(mutableListOf()) {
            startActivity(DetailTvShowActivity.newIntent(requireContext(), it.id))
        }

        recycler_tv.apply {
            adapter = mFilmAdapter
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
                mFilmAdapter.addFilms(it)
            }
        })

        mTvShowViewModel.getErrorLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(mTvShowBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
            }
        })
    }

}
