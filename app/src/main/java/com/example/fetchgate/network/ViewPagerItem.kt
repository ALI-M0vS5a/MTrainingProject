package com.example.fetchgate.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ViewPagerItem(
    val image: String
) : Parcelable