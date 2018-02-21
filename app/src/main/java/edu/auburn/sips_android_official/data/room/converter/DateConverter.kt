package edu.auburn.sips_android_official.data.room.converter

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by clj00 on 2/19/2018.
 */
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (if (date == null) null else date.getTime())?.toLong()
    }
}