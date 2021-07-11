package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mother_languages_table")
data class MotherLanguage (
    val mother_language: String?)
{
    @PrimaryKey
    var id: Int = 1
}
