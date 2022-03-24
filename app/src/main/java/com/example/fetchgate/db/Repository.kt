package com.example.fetchgate.db

import androidx.lifecycle.LiveData
import com.example.fetchgate.network.Add

class Repository(private val itemDao: ItemDao) {

    val allItem: LiveData<List<Add>> = itemDao.getAllItems()

    suspend fun insert(add: Add){
        itemDao.insert(add)
    }
    suspend fun update(add: Add){
        itemDao.update(add)
    }

    suspend fun delete(add: Add){
        itemDao.deleteItem(add)
    }
}