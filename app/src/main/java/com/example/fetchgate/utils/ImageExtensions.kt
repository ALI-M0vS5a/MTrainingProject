package com.example.fetchgate.utils

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide

fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .circleCrop()
            .into(imgView)
    }
}

fun bindImageString(imgView: ImageView, imgUrl: String) {
    imgUrl.let {
        Glide.with(imgView.context)
            .asBitmap()
            .load(it)
            .into(imgView)
    }
}










