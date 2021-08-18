package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lang_locale_table")
data class LangLocale (
    val locale: String?)
{
    @PrimaryKey
    var id: Int = 1
}
