package com.example.fetchgate.network


import android.graphics.Bitmap
import android.graphics.Path
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Entity(tableName = "adding_car_table")
@Parcelize
data class Add(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "name")
    val Name: String,
    @ColumnInfo(name = "image")
    val image: String
) : Parcelable




