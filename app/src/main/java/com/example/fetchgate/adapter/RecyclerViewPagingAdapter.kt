package com.example.fetchgate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.ViewItemBinding
import com.example.fetchgate.network.Result
import com.example.fetchgate.utils.bindImage

class RecyclerViewPagingAdapter :
    PagingDataAdapter<Result, RecyclerViewPagingAdapter.ViewHolder>(ResultDiffUtil()) {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClicked(result: Result)
        fun onImageClicked(imageUrl: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    class ViewHolder(
        val binding: ViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.tvFullName.text = data.full_name
            binding.tvID.text = data.id.toString()

            bindImage(binding.imageview, data.owner.avatar_url)

        }

    }


    class ResultDiffUtil : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { it1 ->
            holder.bind(it1)
            holder.itemView.setOnClickListener {
                mListener.onItemClicked(it1)
            }
            getItem(position)?.let { it2 ->
                holder.binding.imageview.setOnClickListener {
                    mListener.onImageClicked(it2.owner.avatar_url)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }


}


