package com.dicoding.naufal.moviecatalogue.ui.detail.tv

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
import com.dicoding.naufal.moviecatalogue.databinding.ActivityDetailTvShowBinding
import com.dicoding.naufal.moviecatalogue.ui.detail.GenreItemDecoration
import com.dicoding.naufal.moviecatalogue.ui.detail.GenresAdapter
import com.dicoding.naufal.moviecatalogue.ui.detail.ProductionCompaniesAdapter
import com.dicoding.naufal.moviecatalogue.ui.detail.ProductionCompanyItemDecoration
import com.dicoding.naufal.moviecatalogue.utils.*
import com.google.android.material.snackbar.Snackbar
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import kotlinx.android.synthetic.main.activity_detail_tv_show.*
import org.koin.androidx.scope.currentScope
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailTvShowActivity : BaseActivity<ActivityDetailTvShowBinding, DetailTvShowViewModel>() {

    private val mDetailTvShowViewModel: DetailTvShowViewModel by currentScope.viewModel(this)
    private lateinit var mGenresAdapter: GenresAdapter
    private lateinit var mProductionCompaniesAdapter: ProductionCompaniesAdapter
    private lateinit var mCreatorAdapter: CreatorAdapter
    private lateinit var mDetailTvShowBinding: ActivityDetailTvShowBinding
    private var mMenuFav: Menu? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_detail_tv_show
    }

    override fun getViewModel(): DetailTvShowViewModel {
        return mDetailTvShowViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.detailTvShowViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailTvShowBinding = getViewDataBinding()
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        mMenuFav = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        mDetailTvShowViewModel.getIsFavoriteLiveData().value?.let {
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
                if (mDetailTvShowViewModel.getIsFavoriteLiveData().value == true) {
                    mDetailTvShowViewModel.deleteFromFavorite()
                    Toast.makeText(this, "Successfully removed from favorite", Toast.LENGTH_SHORT).show()
                } else {
                    mDetailTvShowViewModel.addToFavorite()
                    Toast.makeText(this, "Successfully added to favorite", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent("UPDATE")
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                intent.component = ComponentName(
                    "com.kurjaka.mobile.favoritewidgetmoviecatalogue",
                    "com.kurjaka.mobile.favoritewidgetmoviecatalogue.ui.widget.FavoriteFilmWidget"
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
        mCreatorAdapter = CreatorAdapter(mutableListOf())

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
            layoutManager = LinearLayoutManager(this@DetailTvShowActivity, RecyclerView.HORIZONTAL, false)
            addItemDecoration(ProductionCompanyItemDecoration(12))
        }

        recycler_creator.apply {
            adapter = mCreatorAdapter
            layoutManager = LinearLayoutManager(this@DetailTvShowActivity, RecyclerView.HORIZONTAL, false)
            addItemDecoration(CreatorItemDecoration(15))
        }


        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mDetailTvShowViewModel.setTvId(intent.getIntExtra(TV_ID, 0))


        mDetailTvShowViewModel.getTvLiveData().observe(this, Observer {
            if (it != null) {
                val date = it.firstAirDate?.let { dateString ->
                    getDateTimeFromString(dateString)
                }
                val runtime = if (it.episodeRunTime?.size != 0 && it.episodeRunTime != null) {
                    it.episodeRunTime?.get(0)?.let { runtime ->
                        getActualRuntime(runtime)
                    }
                } else {
                    getActualRuntime(0)
                }


                txt_title.text = it.title
                txt_year.text = resources.getString(R.string.year, date)
                txt_rate.text = it.voteAverage.toString()
                txt_overview.text = it.overview
                txt_runtime.text = resources.getString(R.string.runtime, runtime?.get("h"), runtime?.get("m"))
                txt_release.text = resources.getString(R.string.date_format, date)
                txt_language.apply {
                    text = resources.getString(getLanguageReference(it.originalLanguage))

                    setCompoundDrawablesWithIntrinsicBounds(getNationFlag(it.originalLanguage), 0, 0, 0)
                }
                txt_original_title.text = it.originalName
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

                if (it.creator?.size != 0 && it.creator != null) {
                    txt_creator_title.visibility = View.VISIBLE
                    mCreatorAdapter.addItems(it.creator!!)
                } else {
                    txt_creator_title.visibility = View.GONE
                }

                it.genres?.let {
                    mGenresAdapter.addItems(it)
                }

            }
        })

        mDetailTvShowViewModel.getErrorLiveData().observe(this, Observer {
            Snackbar.make(mDetailTvShowBinding.root, getString(it), Snackbar.LENGTH_SHORT).show()
        })

        mDetailTvShowViewModel.isLoading().observe(this, Observer {
            if (it) {
                progress_poster.visibility = View.VISIBLE
                progress_backdrop.visibility = View.VISIBLE
            }
        })

        mDetailTvShowViewModel.getIsFavoriteLiveData().observe(this, Observer {
            if (it == true) {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp)
            } else {
                mMenuFav?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp)
            }
        })
    }

    companion object {
        const val TV_ID = "TV_ID"
        fun newIntent(context: Context, tvId: Int): Intent = Intent(context, DetailTvShowActivity::class.java).apply {
            putExtra(TV_ID, tvId)
        }
    }
}
