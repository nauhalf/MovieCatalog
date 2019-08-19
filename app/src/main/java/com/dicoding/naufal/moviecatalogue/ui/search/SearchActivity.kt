package com.dicoding.naufal.moviecatalogue.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseActivity
import com.dicoding.naufal.moviecatalogue.databinding.ActivitySearchBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.movie.DetailMovieActivity
import com.dicoding.naufal.moviecatalogue.ui.detail.tv.DetailTvShowActivity
import com.dicoding.naufal.moviecatalogue.ui.main.FilmItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.template_empty_search.*
import kotlinx.android.synthetic.main.template_toolbar.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {

    private val mSearchViewModel: SearchViewModel by currentScope.viewModel(this)
    private lateinit var mSearchAdapter: SearchAdapter
    private lateinit var mSearchBinding: ActivitySearchBinding
    private lateinit var searchView: SearchView
    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun getViewModel(): SearchViewModel {
        return mSearchViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.searchViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSearchBinding = getViewDataBinding()

        if (savedInstanceState != null) {

        }

        if (intent.hasExtra(SEARCH_TYPE_EXTRA)) {
            mSearchViewModel.searchTypeLiveData.value = intent.getIntExtra(SEARCH_TYPE_EXTRA, 1)
        }

        setUp()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.menu_search)
        searchView = item?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.apply {
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    componentName
                )
            )
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()

                    query?.let { mSearchViewModel.search(it) }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mSearchAdapter = SearchAdapter(onClickListener = {
            if (mSearchViewModel.searchTypeLiveData.value == 1) {
                startActivity(DetailMovieActivity.newIntent(this, it.id))
            } else {
                startActivity(DetailTvShowActivity.newIntent(this, it.id))
            }
        })

        recycler_search.apply {
            adapter = mSearchAdapter
            layoutManager = GridLayoutManager(this@SearchActivity, resources.getInteger(R.integer.discovery_columns))
            addItemDecoration(
                FilmItemDecoration(resources.configuration.orientation, 16)
            )
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mSearchViewModel.listResultLiveData.observe(this, Observer {
            if (it != null) {
                mSearchAdapter.addFilms(it)
            }

            if (it.isEmpty() && !mSearchViewModel.keywordLiveData.value.isNullOrEmpty()) {
                val spannableBuilder = SpannableStringBuilder()
                    .append(getString(R.string.empty_search))
                    .append(" ${mSearchViewModel.keywordLiveData.value}")

                txt_message.text = spannableBuilder

                layout_empty.visibility = View.VISIBLE

            } else {
                layout_empty.visibility = View.GONE
            }
        })

        mSearchViewModel.exceptionLiveData.observe(this, Observer {
            if (it != null) {
                Log.e(SearchActivity::class.simpleName, it.localizedMessage)
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        mSearchViewModel.isLoading().observe(this, Observer {
            if (it) {
                layout_empty.visibility = View.GONE
            }
        })
    }

    companion object {
        const val SEARCH_TYPE_EXTRA = "SEARCH_TYPE_EXTRA"

        fun newIntent(context: Context, searchType: Int): Intent = Intent(context, SearchActivity::class.java).apply {
            putExtra(SEARCH_TYPE_EXTRA, searchType)
        }
    }
}
