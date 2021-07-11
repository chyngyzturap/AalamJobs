package com.pharos.aalamjobs.data.local.db.cv

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pharos.aalamjobs.data.local.db.cv.entities.Education
import com.pharos.aalamjobs.data.local.db.cv.entities.OtherLanguages
import java.lang.reflect.Type
import java.util.*


class Converters {

    @TypeConverter
    fun stringToListServer(data: String?): List<Education?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val gson = Gson()
        val listType: Type = object :
            TypeToken<List<Education?>?>() {}.type
        return gson.fromJson<List<Education?>>(data, listType)
    }

    @TypeConverter
    fun listServerToString(someObjects: List<Education?>?): String? {
        val gson = Gson()
        return gson.toJson(someObjects)
    }

        @TypeConverter
        fun stringToMap(value: String): Map<String, String> {
            val mapType = object : TypeToken<Map<String, String>>() {}.type
            val gson = Gson()
            return gson.fromJson(value, mapType)
        }

        @TypeConverter
        fun fromStringMap(map: Map<String, String>): String {
            val gson = Gson()
            return gson.toJson(map)
        }

    @TypeConverter
    fun appToString(app: OtherLanguages): String = Gson().toJson(app)

    @TypeConverter
    fun stringToApp(string: String): OtherLanguages = Gson().fromJson(string, OtherLanguages::class.java)

    @TypeConverter
    fun fromString(value: String?): Array<String> {
        val listType = object :
            TypeToken<Array<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArray(list: Array<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }


//    @TypeConverter
//    fun fromString(value: String?): Map<String?, String?>? {
//        val mapType: Type = object : TypeToken<Map<String?, Any>?>() {}.getType()
//        return Gson().fromJson(value, mapType)
//    }
//
//    @TypeConverter
//    fun fromStringMap(map: Map<String?, Any?>?): String? {
//        val gson = Gson()
//        return gson.toJson(map)
//    }

}
