package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "social_table")
data class Social(
    val facebook: String,
    val linkedin: String,
    val twitter: String,
    val whatsapp: String,
    val telegram: String
){
    @PrimaryKey
    var id: Int = 1
}