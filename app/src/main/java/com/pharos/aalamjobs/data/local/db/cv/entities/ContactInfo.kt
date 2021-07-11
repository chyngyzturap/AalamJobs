package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_info_table")
data class ContactInfo(
    val phone: String?,
    val email: String?,
    val current_country: String?,
    val current_city: String?,
    val living_address: String?
) {
    @PrimaryKey
    var id: Int = 1
}