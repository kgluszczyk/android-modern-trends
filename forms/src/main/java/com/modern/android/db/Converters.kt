package com.modern.android.forms.db

import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class DbConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return if (value == null) null else LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? = date?.toEpochDay()

}

class MoshiConverters {
    @ToJson
    fun toJson(value: LocalDate): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return FORMATTER.parse(value, LocalDate.FROM)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

}