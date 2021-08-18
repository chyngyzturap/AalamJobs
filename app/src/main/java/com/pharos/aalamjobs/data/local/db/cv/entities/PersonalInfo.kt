package com.pharos.aalamjobs.data.local.db.cv.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personal_info_table")
data class PersonalInfo (
    val photo: Uri?,
    val firstname: String?,
    val lastname: String?,
    val middlename: String?,
    val birthdate: String?,
    val citizenship: String?,
    val marital_status: String?,
    val gender: String?
) {
    @PrimaryKey
    var id: Int = 1
}