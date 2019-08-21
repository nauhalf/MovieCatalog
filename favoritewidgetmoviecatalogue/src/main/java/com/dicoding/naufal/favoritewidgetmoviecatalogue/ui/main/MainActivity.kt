package com.dicoding.naufal.favoritewidgetmoviecatalogue.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.favoritewidgetmoviecatalogue.BR
import com.dicoding.naufal.favoritewidgetmoviecatalogue.R
import com.dicoding.naufal.favoritewidgetmoviecatalogue.base.BaseActivity
import com.dicoding.naufal.favoritewidgetmoviecatalogue.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.template_toolbar.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mMainBinding: ActivityMainBinding
    private lateinit var mMainAdapter: MainAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        return mMainViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.mainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding = getViewDataBinding()
        setUp()
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        mMainAdapter = MainAdapter(onClickListener = {

        })
        recycler_main.apply {
            adapter = mMainAdapter
            layoutManager = GridLayoutManager(this@MainActivity, resources.getInteger(R.integer.discovery_columns))
            addItemDecoration(
                FilmItemDecoration(resources.configuration.orientation, 16)
            )
        }

        swap_refersh.setOnRefreshListener {
            mMainViewModel.fetchData()
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mMainViewModel.favFilmLiveData.observe(this, Observer {
            if(it != null){
                mMainAdapter.addFilms(it)
            }
        })

        mMainViewModel.isLoading().observe(this, Observer {
            if(it == false){
                swap_refersh.isRefreshing = false
            }
        })
    }
}
