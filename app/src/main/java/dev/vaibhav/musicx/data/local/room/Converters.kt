package dev.vaibhav.musicx.data.local.room

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListOfString(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun fromStringToList(string: String?): List<String>? = string?.split(",")

    @TypeConverter
    fun fromUri(uri: Uri) = uri.toString()

    @TypeConverter
    fun toUri(uriString: String) = uriString.toUri()
}
