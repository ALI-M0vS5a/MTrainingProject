package com.example.fetchgate.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index


@Entity(tableName = "contacts")
data class ContactDataTransfer(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "number")
    var number: String,
    @ColumnInfo(name = "image")
    var image: String?=null
)



