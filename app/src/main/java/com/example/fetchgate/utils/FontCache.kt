package com.example.fetchgate.utils

import android.content.Context
import android.graphics.Typeface
import java.lang.Exception

object FontCache {
    private val fontCache = HashMap<String, Typeface>()

    fun getTypeface(fontName: String, context: Context): Typeface? {
        var typeface = fontCache[fontName]

        if(typeface == null){
            try {
                typeface = Typeface.createFromAsset(context.assets, fontName)
            }catch (e: Exception){
                return null
            }
            fontCache[fontName] = typeface
        }
        return typeface
    }
}