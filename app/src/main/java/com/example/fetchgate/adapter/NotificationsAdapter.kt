package com.example.fetchgate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchgate.databinding.NotificationBinding
import com.example.fetchgate.network.Notifications

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>(){
    val notifications = ArrayList<Notifications>()

    inner class NotificationViewHolder(val binding: NotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notifications: Notifications) {
            with(binding) {
                title.text = notifications.notificationTitle
                message.text = notifications.message
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            NotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Notifications>) {
        notifications.clear()
        notifications.addAll(newList)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}