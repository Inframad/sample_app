package com.test.a2021_q4_tyukavkin.data.datasource.local

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDatasource
@Inject constructor(
    context: Context
) {

    companion object {
        private const val DATA = "DATA"
    }

    private val sharedPref = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)

    suspend fun saveString(value: String?) {
        withContext(Dispatchers.IO) {
            with(sharedPref.edit()) {
                putString("TOKEN", value)
                apply()
            }
        }
    }

    fun getString(key: String): String? =
            sharedPref.getString(key, null)

}