package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.ItemViewPagerBinding
import com.example.fetchgate.network.ViewPager
import com.example.fetchgate.utils.bindImageString


class ViewPagerAdapter(
    private val views: ArrayList<ViewPager>


) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private lateinit var mListener:OnItemClickListener

    class ViewPagerViewHolder(val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewpager: ViewPager) {
            bindImageString(binding.imageViewCar, viewpager.image)
        }
    }

    interface OnItemClickListener{
        fun onLongClick(viewpager: ViewPager)

    }

    fun setOnItemClickListener(listener:OnItemClickListener) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder(
            ItemViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateViewPagerList(viewpager: ViewPager) {
        views.add(viewpager)
        notifyDataSetChanged()

    }
    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: ViewPager){
        views.remove(position)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.bind(views[position])
        getItemId(position).let {
            holder.itemView.setOnLongClickListener {
                mListener.onLongClick(views[position])
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return views.size

    }
}