package com.example.fetchgate.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")
data class Notifications(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "title")
    val notificationTitle: String,
    @ColumnInfo(name = "message")
    val message: String
)