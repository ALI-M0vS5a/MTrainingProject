package com.example.fetchgate.add

import android.app.Application
import androidx.lifecycle.*
import com.example.fetchgate.db.ItemDao
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.db.Repository
import com.example.fetchgate.network.Add
import kotlinx.coroutines.*


class AddViewModel(val database: ItemDao, application: Application) :
    AndroidViewModel(application) {

    val allItem: LiveData<List<Add>>
    private val repository: Repository

    init {
        val dao = ItemDatabase.getInstance(application).itemDao
        repository = Repository(dao)
        allItem = repository.allItem
        application.applicationContext

    }

    fun delete(add: Add) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(add)
    }

    fun updateItem(add: Add) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(add)
    }

    fun addItem(add: Add) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(add)

    }


}