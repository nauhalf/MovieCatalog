package com.dicoding.naufal.moviecatalogue.utils

import android.content.Context
import com.dicoding.naufal.moviecatalogue.R
import com.dicoding.naufal.moviecatalogue.data.remote.network.Result
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

fun getLanguageReference(lang: String?): Int {
    return when (lang?.toLowerCase()) {
        "ja" -> R.string.japanese
        else -> R.string.undefined_language
    }
}

fun getNationFlag(lang: String?): Int {
    return when (lang?.toLowerCase()) {
        "ja" -> R.drawable.japan_flag
        else -> R.drawable.japan_flag
    }
}

fun getActualRuntime(minutes: Int): HashMap<String, Int> {
    val map = HashMap<String, Int>()
    try {
        val h = minutes / 60
        val m = minutes % (h * 60)
        map["h"] = h
        map["m"] = m
    } catch (e: ArithmeticException) {
        map["h"] = 0
        map["m"] = 0
    }
    return map
}


fun getDateTimeFromString(dateString: String): Date? {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.parse(dateString)
    } catch (e: ParseException) {
        null
    }
}

//SUBMISSION 2hom
//fun Context.parseJsonFromRes(source: Int): String {
//    return this.resources.openRawResource(source)
//        .bufferedReader().use { it.readText() }
//}

suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> = try {
    call.invoke()
} catch (e: Exception) {
    Result.Error(IOException(e))
}