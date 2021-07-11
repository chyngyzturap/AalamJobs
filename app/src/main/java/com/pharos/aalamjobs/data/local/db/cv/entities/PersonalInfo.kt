package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_info_table")
data class PersonalInfo (
    val photo: String,
    val firstname: String,
    val lastname: String,
    val middlename: String,
    val birthdate: String,
    val citizenship: String,
    val marital_status: String,
    val gender: String
) {
    @PrimaryKey
    var id: Int = 1
}