package com.example.fetchgate.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.db.Repository
import com.example.fetchgate.network.Notifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    val allNotifications: LiveData<List<Notifications>>
    private val repository: Repository

    init {
        val dao = ItemDatabase.getInstance(application).itemDao
        repository = Repository(dao)
        allNotifications = repository.allNotifications
        application.applicationContext
    }

    fun addNotification(notifications: Notifications) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNotifications(notifications)
    }
    fun deleteNotification(notifications: Notifications) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteNotification(notifications)
    }
}