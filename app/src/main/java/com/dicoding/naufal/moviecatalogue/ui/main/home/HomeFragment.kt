package com.dicoding.naufal.moviecatalogue.ui.main.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.naufal.moviecatalogue.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            setUp()
    }

    private fun setUp() {

        val pagerAdapter =
            HomePagerAdapter(childFragmentManager, requireContext())

        viewpager_home.adapter = pagerAdapter
        viewpager_home.offscreenPageLimit = 2
        tab_home.setupWithViewPager(viewpager_home)
        tab_home.getTabAt(0)?.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_movie_black_24dp)
        tab_home.getTabAt(1)?.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_live_tv_black_24dp)

    }
}
