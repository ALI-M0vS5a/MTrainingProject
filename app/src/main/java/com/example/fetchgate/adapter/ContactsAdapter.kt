package com.example.fetchgate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fetchgate.R
import com.example.fetchgate.databinding.ContactsLayoutBinding
import com.example.fetchgate.network.ContactDataTransfer
import java.util.*
import kotlin.collections.ArrayList

class ContactsAdapter :
    ListAdapter<ContactDataTransfer, ContactsAdapter.ContactHolder>(DIFF_ITEM_CALLBACK),
    SectionIndexer {

    private val mDataArray: List<String>? = null
    private var mSectionPositions: ArrayList<Int>? = null

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<ContactDataTransfer>() {
            override fun areItemsTheSame(
                oldItem: ContactDataTransfer,
                newItem: ContactDataTransfer
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: ContactDataTransfer,
                newItem: ContactDataTransfer
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    class ContactHolder(val binding: ContactsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contacts: ContactDataTransfer) {
            with(binding) {
                tvName.text = contacts.name.toString()
                tvNumber.text = contacts.number.toString()

                Glide.with(itemView.context)
                    .load(R.drawable.ic_contact)
                    .into(ivPerson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder(
            ContactsLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(getItem(position))
    }
    override fun getSections(): Array<out Any> {
        val sections: MutableList<String> = ArrayList(26)
        mSectionPositions = ArrayList(26)
        var i = 0
        val size = mDataArray!!.size
        while (i < size) {
            val section = mDataArray[i][0].toString().uppercase(Locale.getDefault())
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions!!.add(i)
            }
            i++
        }
        return sections.toTypedArray()
    }

    override fun getPositionForSection(p0: Int): Int {
       return mSectionPositions!![p0]
    }

    override fun getSectionForPosition(p0: Int): Int {
        return 0
    }
}