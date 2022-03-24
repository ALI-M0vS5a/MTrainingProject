package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.AddItemBinding
import com.example.fetchgate.network.Add
import com.example.fetchgate.utils.bindImageString


class AddRecyclerViewAdapter : RecyclerView.Adapter<AddRecyclerViewAdapter.AddViewHolder>() {
    private val adds = ArrayList<Add>()

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onDeleteIconClicked(add: Add)
        fun onItemClicked(add: Add)
        fun onImageClicked(imageUrl: String)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    class AddViewHolder(val binding: AddItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(add: Add) {
            binding.textViewName.text = add.Name
            bindImageString(binding.imageview,add.image)
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
            holder.binding.deleteCard.setOnClickListener {
                mListener.onDeleteIconClicked(adds[position])

            }
        }
        getItemId(position).let {
            holder.itemView.setOnClickListener {
                mListener.onItemClicked(adds[position])
            }

        }
        getItemId(position).let {
            holder.binding.imageview.setOnClickListener {
                mListener.onImageClicked(adds[position].image)
            }
        }

    }

    override fun getItemCount(): Int {
        return adds.size
    }


}

