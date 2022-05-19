package com.example.fetchgate.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.db.Repository
import com.example.fetchgate.network.ContactDataTransfer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    val allContacts: LiveData<List<ContactDataTransfer>>
    private val repository: Repository


    init {
        val dao = ItemDatabase.getInstance(application).itemDao
        repository = Repository(dao)
        allContacts = repository.allContacts
        application.applicationContext
    }

    fun addContacts(contactDataTransfer: ContactDataTransfer) = viewModelScope.launch(Dispatchers.IO){
        repository.insertContacts(contactDataTransfer)
    }
}