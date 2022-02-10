package com.test.a2021_q4_tyukavkin.data.converter

import com.squareup.moshi.Moshi
import retrofit2.HttpException

fun <T> HttpException.convertErrorBody(convertClass: Class<T>): T? {
    return try {
        this.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(convertClass)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}