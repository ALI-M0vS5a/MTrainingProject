package com.example.fetchgate.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fetchgate.network.Add
import com.example.fetchgate.network.Notifications

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(add: Add)

    @Update
    suspend fun update(add: Add)

    @Delete
    suspend fun deleteItem(add: Add)

    @Query("SELECT * FROM adding_car_table " + "ORDER BY id DESC")
    fun getAllItems(): LiveData<List<Add>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotifications(notifications: Notifications)

    @Query("SELECT * FROM notification " + "ORDER BY id DESC")
    fun getAllNotifications(): LiveData<List<Notifications>>

    @Delete
    suspend fun deleteNotification(notifications: Notifications)


}