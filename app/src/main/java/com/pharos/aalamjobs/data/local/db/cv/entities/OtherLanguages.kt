package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "other_languages_table")
data class OtherLanguages(
    val name: String?,
    val listening: String?,
    val reading: String?,
    val writing: String?,
    val talking: String?
){

    @PrimaryKey
    var id: Int = 1
}
