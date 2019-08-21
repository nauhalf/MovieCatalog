package com.dicoding.naufal.moviecatalogue.ui.detail.movie

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.naufal.moviecatalogue.BR
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.base.BaseActivity
import com.dicoding.naufal.moviecatalogue.databinding.ActivityDetailMovieBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.GenreItemDecoration
import com.dicoding.naufal.moviecatalogue.ui.detail.GenresAdapter
import com.dicoding.naufal.moviecatalogue.ui.detail.ProductionCompaniesAdapter
import com.dicoding.naufal.moviecatalogue.ui.detail.ProductionCompanyItemDecoration
import com.dicoding.naufal.moviecatalogue.utils.*
import com.google.android.material.snackbar.Snackbar
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import kotlinx.android.synthetic.main.activity_detail_movie.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailMovieActivity : BaseActivity<ActivityDetailMovieBinding, DetailMovieViewModel>() {

    private val mDetailMovieViewModel: DetailMovieViewModel by currentScope.viewModel(this)
    private lateinit var mGenresAdapter: GenresAdapter
    private lateinit var mProductionCompaniesAdapter: ProductionCompaniesAdapter
    private lateinit var mDetailMovieBinding: ActivityDetailMovieBinding
    private var mMenuFav: Menu? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_movie
    }

    override fun getViewModel(): DetailMovieViewModel {
        return mDetailMovieViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailMovieViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailMovieBinding = getViewDataBinding()
        setUp()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        mMenuFav = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        mDetailMovieViewModel.getIsFavoriteLiveData().value?.let {
            if (it) {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
            } else {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }


            R.id.menu_favorite -> {
                if (mDetailMovieViewModel.getIsFavoriteLiveData().value == true) {
                    mDetailMovieViewModel.deleteFromFavorite()
                    Toast.makeText(this, getString(R.string.success_add_favorite), Toast.LENGTH_SHORT).show()
                } else {
                    mDetailMovieViewModel.addToFavorite()
                    Toast.makeText(this, getString(R.string.success_remove_favorite), Toast.LENGTH_SHORT).show()
                }
                val intent = Intent("WIDGET_UPDATE")
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                intent.component = ComponentName(
                    "com.dicoding.naufal.favoritewidgetmoviecatalogue",
                    "com.dicoding.naufal.favoritewidgetmoviecatalogue.ui.widget.FavoriteFilmWidget"
                )
                sendBroadcast(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mGenresAdapter = GenresAdapter(mutableListOf())
        mProductionCompaniesAdapter = ProductionCompaniesAdapter(mutableListOf())

        recycler_genres.apply {
            adapter = mGenresAdapter
            layoutManager = FlowLayoutManager().apply {
                isAutoMeasureEnabled = true
                setAlignment(Alignment.LEFT)
            }
            addItemDecoration(GenreItemDecoration(5))
        }

        recycler_production_companies.apply {
            adapter = mProductionCompaniesAdapter
            layoutManager = LinearLayoutManager(this@DetailMovieActivity, RecyclerView.HORIZONTAL, false)
            addItemDecoration(ProductionCompanyItemDecoration(12))
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailMovieViewModel.setMovieId(intent.getIntExtra(MOVIE_ID_EXTRA, 0))

        mDetailMovieViewModel.getMovieLiveData().observe(this, Observer {
            if (it != null) {
                val date = it.releaseDate?.let { dateString ->
                    getDateTimeFromString(dateString)
                }
                val runtime = getActualRuntime(it.runtime)

                txt_title.text = it.title
                txt_year.text = resources.getString(R.string.year, date)
                txt_rate.text = it.voteAverage.toString()
                txt_overview.text = it.overview
                txt_runtime.text = resources.getString(R.string.runtime, runtime["h"], runtime["m"])
                txt_release.text = resources.getString(R.string.date_format, date)
                txt_language.apply {
                    text = resources.getString(getLanguageReference(it.originalLanguage))

                    setCompoundDrawablesWithIntrinsicBounds(getNationFlag(it.originalLanguage), 0, 0, 0)
                }
                txt_original_title.text = it.originalTitle
                txt_status.text = it.status

                if (it.homepage.isNullOrEmpty()) {
                    txt_homepage.visibility = View.GONE
                } else {
                    txt_homepage.visibility = View.VISIBLE
                    txt_homepage.setOnClickListener { _ ->

                        val intentBuilder = CustomTabsIntent.Builder()
                        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        intentBuilder.build().launchUrl(this, Uri.parse(it.homepage))
                    }
                }

                Glide.with(this)
                    .load(MovieUtils.getImagePoster(it.posterPath))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progress_poster.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progress_poster.visibility = View.GONE
                            return false
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(img_poster)

                Glide.with(this)
                    .load(MovieUtils.getImagePoster(it.backdropPath))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progress_backdrop.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progress_backdrop.visibility = View.GONE
                            return false
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(img_backdrop)

                if (it.productionCompanies?.size != 0 && it.productionCompanies != null) {
                    txt_production_company_title.visibility = View.VISIBLE
                    mProductionCompaniesAdapter.addItems(it.productionCompanies!!)
                } else {
                    txt_production_company_title.visibility = View.GONE
                }

                it.genres?.let {
                    mGenresAdapter.addItems(it)
                }

            }
        })

        mDetailMovieViewModel.getErrorLiveData().observe(this, Observer {

            Snackbar.make(mDetailMovieBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
        })

        mDetailMovieViewModel.isLoading().observe(this, Observer {
            if (it) {
                progress_poster.visibility = View.VISIBLE
                progress_backdrop.visibility = View.VISIBLE
            }
        })

        mDetailMovieViewModel.getIsFavoriteLiveData().observe(this, Observer {
            if (it) {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
            } else {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
            }
        })
    }

    companion object {
        const val MOVIE_ID_EXTRA = "MOVIE_ID"

        fun newIntent(context: Context, movieId: Int): Intent = Intent(context, DetailMovieActivity::class.java).apply {
            putExtra(MOVIE_ID_EXTRA, movieId)
        }
    }
}
