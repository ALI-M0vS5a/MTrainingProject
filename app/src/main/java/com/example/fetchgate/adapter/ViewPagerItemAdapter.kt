package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.ItemViewPager2Binding
import com.example.fetchgate.network.ViewPagerItem
import com.example.fetchgate.utils.bindImageString


class ViewPagerItemAdapter(
    private val views: ArrayList<ViewPagerItem>
) : RecyclerView.Adapter<ViewPagerItemAdapter.ViewPagerItemViewHolder>() {

    class ViewPagerItemViewHolder(val binding: ItemViewPager2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewPagerItem: ViewPagerItem) {
            bindImageString(binding.imageViewViewPager2, viewPagerItem.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerItemViewHolder {
        return ViewPagerItemViewHolder(
            ItemViewPager2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateViewPagerList(viewPagerItem: ViewPagerItem) {
        views.add(viewPagerItem)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        views.clear()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ViewPagerItemViewHolder, position: Int) {
        holder.bind(views[position])
    }

    override fun getItemCount(): Int {
        return views.size
    }
}