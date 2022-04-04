package com.example.fetchgate.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun toList(string: String?): List<String>? {
        return Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)
    }
}