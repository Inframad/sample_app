package com.test.a2021_q4_tyukavkin.util

import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStreamReader

fun readStringFromFile(fileName: String): String {
    val inputStream = InstrumentationRegistry.getInstrumentation()
        .context.assets.open(fileName)
    val builder = StringBuilder()
    val reader = InputStreamReader(inputStream, "UTF-8")
    reader.readLines().forEach {
        builder.append(it)
    }
    return builder.toString()
}