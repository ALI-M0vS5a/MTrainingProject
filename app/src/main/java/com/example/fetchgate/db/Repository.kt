package com.example.fetchgate.db

import androidx.lifecycle.LiveData
import com.example.fetchgate.network.Add
import com.example.fetchgate.network.Notifications

class Repository(private val itemDao: ItemDao) {

    val allItem: LiveData<List<Add>> = itemDao.getAllItems()
    val allNotifications : LiveData<List<Notifications>> = itemDao.getAllNotifications()

    suspend fun insert(add: Add){
        itemDao.insert(add)
    }

    suspend fun update(add: Add){
        itemDao.update(add)
    }

    suspend fun delete(add: Add){
        itemDao.deleteItem(add)
    }

    suspend fun insertNotifications(notifications: Notifications){
        itemDao.insertNotifications(notifications)
    }
    suspend fun deleteNotification(notifications: Notifications){
        itemDao.deleteNotification(notifications)
    }

}