package com.example.fetchgate.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ViewPager(
    val image: String
        ) : Parcelable