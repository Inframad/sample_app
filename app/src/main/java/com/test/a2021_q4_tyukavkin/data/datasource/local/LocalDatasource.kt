package com.test.a2021_q4_tyukavkin.data.datasource.local

import android.content.Context
import javax.inject.Inject

class LocalDatasource
@Inject constructor(
    context: Context
) {

    companion object {
        private const val DATA = "DATA"
    }

    private val sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)

    fun saveString(value: String?) {
        with (sharedPref.edit()) {
            putString("TOKEN", value)
            apply()
        }
    }

    fun getString(key: String): String? =
        sharedPref.getString(key, null)

}