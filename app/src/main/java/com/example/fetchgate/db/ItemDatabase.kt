package com.example.fetchgate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fetchgate.network.Add
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [Add::class],
    version = 21,
    exportSchema = false
)
@TypeConverters(com.example.fetchgate.utils.Converter::class)
abstract class ItemDatabase : RoomDatabase() {
    abstract val itemDao: ItemDao

    companion object {

        @Volatile
        private var INSTANCE: ItemDatabase? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): ItemDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "car_list_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
