package com.gotech.youtube.database.typeconverters

import androidx.room.TypeConverter
import com.naveen.matrimony.model.User
import com.naveen.matrimony.utils.JsonUtils

class UserTypeConverter {

    @TypeConverter
    fun fromItem(value: User): String {
        return JsonUtils.toJson(value)
    }

    @TypeConverter
    fun toItem(value: String): User {
        return JsonUtils.parseJson(value)
    }

    @TypeConverter
    fun fromList(value: ArrayList<String>): String {
        return JsonUtils.toJson(value)
    }

    @TypeConverter
    fun toList(value: String): ArrayList<String> {
        return JsonUtils.parseJson(value)
    }

    @TypeConverter
    fun fromIntList(value: ArrayList<Int>): String {
        return JsonUtils.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): ArrayList<Int> {
        return JsonUtils.parseJson(value)
    }

}