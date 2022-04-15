package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.AddItemBinding
import com.example.fetchgate.network.Add
import com.example.fetchgate.network.ViewPagerItem
import com.google.android.material.tabs.TabLayoutMediator


class AddRecyclerViewAdapter :
    RecyclerView.Adapter<AddRecyclerViewAdapter.AddViewHolder>() {
    private var adds = ArrayList<Add>()

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(add: Add)
        fun onItemLongClick(add: Add)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    class AddViewHolder(val binding: AddItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var viewPagerRecyclerViewItemAdapter: ViewPagerItemAdapter
        private val imagesItem = ArrayList<ViewPagerItem>()

        @SuppressLint("NotifyDataSetChanged")
        fun bind(add: Add) {

            with(binding) {
                viewPagerRecyclerViewItemAdapter = ViewPagerItemAdapter(imagesItem)
                slidePager.adapter = viewPagerRecyclerViewItemAdapter

                textViewName.text = add.Name
                textViewPhoneNumber.text = add.Phone
                textViewEmail.text = add.Email

                viewPagerRecyclerViewItemAdapter.clear()
                for (i in add.image!!) {
                    Log.d("viewpager", i)
                    val images = ViewPagerItem(i)
                    viewPagerRecyclerViewItemAdapter.updateViewPagerList(images)
                }
                TabLayoutMediator(binding.tabLayout, binding.slidePager) { _, _ -> }.attach()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        return AddViewHolder(
            AddItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Add>) {
        adds.clear()
        adds.addAll(newList)
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(
        holder: AddViewHolder, position: Int
    ) {
        holder.bind(adds[position])

        getItemId(position).let {
            holder.itemView.setOnClickListener {
                mListener.onItemClicked(adds[position])
            }
            holder.itemView.setOnLongClickListener {
                mListener.onItemLongClick(adds[position])
                return@setOnLongClickListener true
            }
        }
    }

    override fun getItemCount(): Int {
        return adds.size
    }

}


