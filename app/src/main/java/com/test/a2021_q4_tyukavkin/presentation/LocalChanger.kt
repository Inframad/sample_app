package com.test.a2021_q4_tyukavkin.presentation

import android.content.Context
import android.content.res.Configuration
import java.util.*

object LocaleChanger {

    fun wrapContext(context: Context): Context {

        val savedLocale =
            context.getSharedPreferences("lang", Context.MODE_PRIVATE)
                .getString("lang", null)?.let { Locale(it, "rUS") } //TODO Country
                ?: return context

        Locale.setDefault(savedLocale)

        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        return context.createConfigurationContext(newConfig)
    }
}