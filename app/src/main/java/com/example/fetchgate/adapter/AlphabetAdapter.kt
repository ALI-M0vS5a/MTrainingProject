package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.R
import com.example.fetchgate.utils.AlphabetClickListener

class AlphabetAdapter(
    private val alphabetList: List<Char>,
    private val mContext: Context,
    private val alphaClickListener: AlphabetClickListener
) : RecyclerView.Adapter<AlphabetAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val mView = layoutInflater.inflate(R.layout.alphabets_layout, parent, false)
        return MyViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return alphabetList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {


        holder.textView.text = alphabetList[position].toString()
        holder.textView.setOnClickListener {
            alphaClickListener.onAlphabetClicked(alphabetList[position])
            rowIndex = position
            this.notifyDataSetChanged()

        }
        if (rowIndex == position) {
            holder.textView.textSize = 25F
            holder.textView.typeface = Typeface.DEFAULT_BOLD
        } else {
            holder.textView.textSize = 15F
            holder.textView.typeface = Typeface.DEFAULT
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var textView: TextView = itemView.findViewById(R.id.tv_word)

    }

    companion object {
        private var rowIndex: Int = -1
    }


}

