package com.example.fetchgate.utils

import android.content.Context
import android.util.AttributeSet

class MultiRegularEditText :androidx.appcompat.widget.AppCompatEditText{
    constructor(context: Context) : super(context)

    private fun applyCustomFont(font: Context) {
        val customFont = FontCache.getTypeface("Muli-Regular.ttf", font)
        typeface = customFont
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyCustomFont(context)
    }
}