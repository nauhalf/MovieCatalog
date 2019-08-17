package com.dicoding.naufal.moviecatalogue.ui.detail

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.model.ProductionCompany
import com.dicoding.naufal.moviecatalogue.utils.MovieUtils
import kotlinx.android.synthetic.main.item_production_company.view.*

class ProductionCompaniesAdapter(private val list: MutableList<ProductionCompany>) :
    RecyclerView.Adapter<ProductionCompaniesAdapter.ProductionCompanyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductionCompanyViewHolder {
        return ProductionCompanyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_production_company, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductionCompanyViewHolder, position: Int) {
        holder.bind(list[position])
    }


    fun addItems(data: List<ProductionCompany>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }


    inner class ProductionCompanyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(productionCompany: ProductionCompany){
            if(!productionCompany.logoPath.isNullOrEmpty()){
                Glide.with(view.context)
                    .load(MovieUtils.getCompanyLogo(productionCompany.logoPath))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .fitCenter()
                    .into(view.img_company)
            } else {
                view.img_company.background = view.context.getDrawable(R.drawable.image_production_company_no_image_background)
                view.img_company.clipToOutline = true
                view.img_company.setImageDrawable(view.context.getDrawable(R.drawable.ic_no_photo))
            }

            view.txt_company_name.text = productionCompany.name
        }
    }
}