package com.example.fetchgate.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*


class ContextUtils(base: Context?) : ContextWrapper(base)

    fun updateLocale(context: Context, localeToSwitchTo: Locale): ContextWrapper {
        var context1 = context
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(localeToSwitchTo)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        } else {
            configuration.setLocale(localeToSwitchTo)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context1 = context.createConfigurationContext(configuration)
        } else {
            context.createConfigurationContext(configuration)
        }
        return ContextUtils(context1)
    }
