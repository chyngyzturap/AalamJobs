package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "education_table3")
data class Education3(
    val institution: String?,
    val date_from: String?,
    val date_to: String?,
    val specialization: String,
    val city: String,
    val country: String
){
    @PrimaryKey
    var id: Int = 1
}