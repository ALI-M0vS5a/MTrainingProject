package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SectionIndexer
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.example.fetchgate.databinding.ContactsLayoutBinding
import com.example.fetchgate.network.ContactDataTransfer
import java.util.*


class ContactsAdapter :
    RecyclerView.Adapter<ContactsAdapter.ContactHolder>(), SectionIndexer {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onImageClicked(imgUrl: String)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.mListener = listener
    }
    private var contacts = ArrayList<ContactDataTransfer>()

    private var mSectionPositions: ArrayList<Int>? = null

    class ContactHolder(val binding: ContactsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contacts: ContactDataTransfer) {
            with(binding) {
                contactName.text = contacts.name
                contactNumber.text = contacts.number
                val letters = contacts.name.split(" ").toList()
                if (letters.size >= 2) {
                    val drawable = TextDrawable.builder()
                        .buildRect(letters[0].first().toString() + letters[1].first(), Color.LTGRAY)
                    contactTmb.setImageDrawable(drawable)
                } else {
                    val drawable = TextDrawable.builder()
                        .buildRect(letters[0].first().toString(), Color.LTGRAY)
                    contactTmb.setImageDrawable(drawable)
                }
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

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ContactDataTransfer>) {
        contacts.clear()
        contacts.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(contacts[position])
        getItemId(position).let {
            holder.binding.contactTmb.setOnClickListener {
                mListener.onImageClicked(it.toString())
            }
        }
    }


    override fun getSections(): Array<out Any?> {
        val sections: MutableList<String> = ArrayList()
        mSectionPositions = ArrayList()
        var i = 0
        val size = contacts.size
        while (i < size) {
            val section: String =
                java.lang.String.valueOf(contacts[i].name.first()).uppercase(Locale.getDefault())
            if (!sections.contains(section)) {
                sections.add(section)
                mSectionPositions!!.add(i)
            }
            i++
        }
        return sections.toTypedArray()
    }


    override fun getPositionForSection(i: Int): Int {
        return mSectionPositions!![i]
    }

    override fun getSectionForPosition(i: Int): Int {
        return 0
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}
